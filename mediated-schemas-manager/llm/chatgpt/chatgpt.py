import json
import os
from os.path import dirname, join

import openai


class ChatGPT:

    def __init__(self):
        self.key = self.__get_key()

    def get_solution(self, task="", prompt=""):
        openai.api_key = self.key

        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": task},
                {"role": "user", "content": prompt}
            ],
            temperature=1
        )

        return response

    def __get_key(self):
        project_root = dirname(dirname(__file__))
        output_path = join(project_root, '../config/')
        api_key = ""

        with open(output_path + "/secrets.json") as f:
            secrets = json.load(f)
            api_key = secrets["api_key"]

        return api_key
