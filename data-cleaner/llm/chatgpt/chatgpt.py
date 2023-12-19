import json
import time

import openai
import logging

from os.path import dirname, join
from timeout_decorator import timeout

from enums.chatgpt.Role import Role
from enums.llm.DefaultAgentAnswers import DefaultAgentAnswers
from llm.GenericLLMApi import GenericLLMApi
from llm.PromptBuilder import PromptBuilder


class ChatGPT(GenericLLMApi):

    def __init__(self, model):
        self.__key = self.__get_key()
        self.__model = model

    def get_simple_solution(self, task="", prompt=""):
        max_retries = 3
        current_retry = 0

        while current_retry < max_retries:
            try:
                solution = json.loads(self.__get_simple_solution_text(task, prompt))
                return solution
            except json.decoder.JSONDecodeError as e:
                logging.error(f"JSON decoding error: {e}")
                current_retry += 1
            except Exception as e:
                logging.error(f"Unknonwn exception occurred {e}")
                current_retry += 1

            if current_retry < max_retries:
                logging.info("Retrying call to __chatgpt API...")
                time.sleep(5)
            else:
                logging.error("Maximum retries reached. Unable to get a solution.")
                break

        return None

    @timeout(20)
    def __get_simple_solution_text(self, task="", prompt=""):
        response = openai.ChatCompletion.create(
            model=self.__model.value,
            messages=[
                {"role": Role.SYSTEM.value, "content": task},
                {"role": Role.USER.value, "content": prompt}
            ],
            temperature=1
        )

        response_content = response.choices[0].message.content

        self.__print_log_prompt(task, prompt, response_content)

        return response_content

    def get_one_shot_solution(self, input_example, output_example, task, prompt, prompt_input, instructions, ontology):
        max_retries = 3
        current_retry = 0

        while current_retry < max_retries:
            try:
                solution_text = self.__get_one_shot_solution_text(input_example, output_example, task, prompt,
                                                                  prompt_input, instructions, ontology)
                solution_text = self.__format_text_response(solution_text)
                solution = json.loads(solution_text)
                return solution
            except json.decoder.JSONDecodeError as e:
                logging.error(f"JSON decoding error: {e}")
                current_retry += 1
            except Exception as e:
                logging.error(f"Unknonwn exception occurred {e}")
                current_retry += 1

            if current_retry < max_retries:
                logging.info("Retrying call to __chatgpt API...")
                time.sleep(5)
            else:
                logging.error("Maximum retries reached. Unable to get a solution.")
                break

        return None

    @timeout(20)
    def __get_one_shot_solution_text(self, input_example, output_example, task, prompt, prompt_input, instructions, ontology):
        prompt_builder = PromptBuilder()

        instructions_subprompt = prompt_builder.build_instructions_subprompt(instructions)
        observations_subprompt = prompt_builder.build_observations_subprompt(ontology)

        prompt += instructions_subprompt
        prompt += observations_subprompt

        prompt_example = prompt_builder.build_prompt_example(input_example, output_example)

        response = openai.ChatCompletion.create(
            model=self.__model.value,
            messages=[
                {"role": Role.SYSTEM.value, "content": task},
                {"role": Role.USER.value, "content": prompt},
                {"role": Role.ASSISTANT.value, "content": DefaultAgentAnswers.INSTRUCTIONS_PROVIDED.value},
                {"role": Role.USER.value, "content": prompt_example},
                {"role": Role.ASSISTANT.value, "content": DefaultAgentAnswers.EXAMPLE_PROVIDED.value},
                {"role": Role.USER.value, "content": prompt_input}
            ],
            temperature=1
        )
        response_content = response.choices[0].message.content

        self.__print_log_prompt(task, prompt, response_content)

        return response_content

    def __get_key(self):
        project_root = dirname(dirname(__file__))
        output_path = join(project_root, '../config/')

        with open(output_path + "/secrets.json") as f:
            secrets = json.load(f)
            api_key = secrets["api_key"]

        openai.api_key = api_key

        return api_key

    def __format_text_response(self, output):
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
