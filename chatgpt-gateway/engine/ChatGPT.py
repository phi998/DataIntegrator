import logging
import time
import openai

from config.ConfigCache import ConfigCache
from config.KeyManager import KeyManager
from engine.PromptBuilder import PromptBuilder

from enums.DefaultAgentAnswers import DefaultAgentAnswers
from enums.Model import Model
from enums.Role import Role


class ChatGPT:

    def __init__(self, model=Model.GPT_3_5_16K):
        self.__model = model
        self.__cache = ConfigCache().get_instance()

        km = KeyManager()
        openai.api_key = km.get_key()

    def interact(self, task, prompt, prompt_input=None, instructions=None, observations=None, examples=None):
        max_retries = self.__cache.get_configuration("max_llm_attempts")
        current_retry = 0

        result = {}

        while current_retry < max_retries:
            try:
                solution_text = self.__get_solution_text(task, prompt, prompt_input, instructions, observations, examples)
                result = {"label": "SUCCESS", "content": solution_text}
                break
            except Exception as e:
                logging.error(f"Unknown exception occurred {e}")
                current_retry += 1

            if current_retry < max_retries:
                logging.info("Retrying call to chatgpt API...")
                time.sleep(self.__cache.get_configuration("llm_fail_timewait_secs"))
            else:
                logging.error("Maximum retries reached. Unable to get a solution.")
                result = {"label": "ERROR", "content": "Maximum retries reached. Unable to get a solution"}
                break

        return result

    def __get_solution_text(self, task, prompt, prompt_input, instructions, observations, examples):
        prompt_builder = PromptBuilder()

        if instructions:
            instructions_subprompt = prompt_builder.build_instructions_subprompt(instructions)
            prompt += instructions_subprompt

        if observations:
            observations_subprompt = prompt_builder.build_observations_subprompt(observations)
            prompt += observations_subprompt

        messages = [
            {"role": Role.SYSTEM.value, "content": task},
            {"role": Role.USER.value, "content": prompt}
        ]

        if prompt_input:
            messages.append({"role": Role.ASSISTANT.value, "content": DefaultAgentAnswers.INSTRUCTIONS_PROVIDED.value})

            if examples:
                for example in examples:
                    input_example = example["input"]
                    output_example = example["output"]
                    messages.append(prompt_builder.build_prompt_example(input_example, output_example))

                messages.append({"role": Role.ASSISTANT.value, "content": DefaultAgentAnswers.EXAMPLE_PROVIDED.value})

            messages.append({"role": Role.USER.value, "content": prompt_input})

        response = openai.ChatCompletion.create(
            model=self.__model.value,
            messages=messages,
            temperature=self.__cache.get_configuration("temperature")
        )
        response_content = response.choices[0].message.content

        self.__print_log_prompt(task, prompt, response_content)

        return response_content

    def __format_json_text_response(self, output):  # deletes useless prose
        index_of_brace = output.find('{')
        if index_of_brace != -1:
            output = output[index_of_brace:]

        index_of_brace = output.find('}')
        if index_of_brace != -1:
            output = output[:index_of_brace + 1]

        return output

    def __print_log_prompt(self, task, prompt, response):
        print("########## CHATGPT START ###########")
        print(f"Task: {task}")
        print(f"Prompt: {prompt}")
        print(f"Solution: {response}")
        print("########## CHATGPT END ###########")
