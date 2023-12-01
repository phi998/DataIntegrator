import json
import time

import openai

from os.path import dirname, join

from enums.chatgpt.Model import Model
from enums.chatgpt.Role import Role
from enums.llm.DefaultAgentAnswers import DefaultAgentAnswers
from llm.GenericLLMApi import GenericLLMApi
from llm.PromptBuilder import PromptBuilder


class ChatGPT(GenericLLMApi):

    def __init__(self):
        self.key = self.__get_key()

    def get_simple_solution(self, model=Model.DEFAULT, task="", prompt=""):
        return json.loads(self.__get_simple_solution_text(model, task, prompt))

    def __get_simple_solution_text(self, model=Model.DEFAULT, task="", prompt=""):
        response = openai.ChatCompletion.create(
            model=model.value,
            messages=[
                {"role": Role.SYSTEM.value, "content": task},
                {"role": Role.USER.value, "content": prompt}
            ],
            temperature=1.6
        )

        return response.choices[0].message.content

    def get_one_shot_solution(self, input_example, output_example, task, prompt, prompt_input, instructions,
                              model=Model.DEFAULT):
        solution_found = False
        solution = ""

        while not solution_found:
            try:
                solution = json.loads(
                    self.__get_one_shot_solution_text(input_example, output_example, task, prompt, prompt_input,
                                                      instructions, model))
                solution_found = True
            except json.decoder.JSONDecodeError as e:
                print("JSON decoding error:", e)
                print("Retrying call to chatgpt api...")
                time.sleep(5)

        return solution

    def __get_one_shot_solution_text(self, input_example, output_example, task, prompt, prompt_input, instructions,
                              model=Model.DEFAULT):
        prompt_builder = PromptBuilder()

        prompt = prompt_builder.build_prompt_with_instructions(prompt, instructions)

        prompt_example = prompt_builder.build_prompt_example(input_example, output_example)

        response = openai.ChatCompletion.create(
            model=model.value,
            messages=[
                {"role": Role.SYSTEM.value, "content": task},
                {"role": Role.USER.value, "content": prompt},
                {"role": Role.ASSISTANT.value, "content": DefaultAgentAnswers.INSTRUCTIONS_PROVIDED.value},
                {"role": Role.USER.value, "content": prompt_example},
                {"role": Role.ASSISTANT.value, "content": DefaultAgentAnswers.EXAMPLE_PROVIDED.value},
                {"role": Role.USER.value, "content": prompt_input}
            ],
            temperature=1.6
        )
        response_content = response.choices[0].message.content

        return response_content

    def __get_key(self):
        project_root = dirname(dirname(__file__))
        output_path = join(project_root, '../config/')

        with open(output_path + "/secrets.json") as f:
            secrets = json.load(f)
            api_key = secrets["api_key"]

        openai.api_key = api_key

        return api_key
