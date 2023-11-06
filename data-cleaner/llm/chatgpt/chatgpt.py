import json
import openai

from os.path import dirname, join

from enums.chatgpt.Model import Model
from enums.chatgpt.Role import Role


class ChatGPT:

    def __init__(self):
        self.key = self.__get_key()

    def get_simple_solution(self, model=Model.DEFAULT, task="", prompt=""):
        openai.api_key = self.key

        response = openai.ChatCompletion.create(
            model=model.value,
            messages=[
                {"role": Role.SYSTEM.value, "content": task},
                {"role": Role.USER.value, "content": prompt}
            ],
            temperature=1.6
        )

        return response.choices[0].message.content

    def get_one_shot_solution(self, model, input_example='', output_example='', task='', prompt='', prompt_input=''):



        pass

    def __get_key(self):
        project_root = dirname(dirname(__file__))
        output_path = join(project_root, '../config/')

        with open(output_path + "/secrets.json") as f:
            secrets = json.load(f)
            api_key = secrets["api_key"]

        return api_key
