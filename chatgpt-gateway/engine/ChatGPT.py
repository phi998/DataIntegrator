import json
from os.path import dirname, join
import openai

from config.ConfigCache import ConfigCache


class ChatGPT:

    def __init__(self, model):
        self.__key = self.__get_key()
        self.__model = model
        self.__cache = ConfigCache().get_instance()

    def interact(self, task, prompt, prompt_input, instructions, observations, examples):

        pass

    def __get_key(self):
        project_root = dirname(dirname(__file__))
        output_path = join(project_root, '../config/')

        with open(output_path + "/secrets.json") as f:
            secrets = json.load(f)
            api_key = secrets["api_key"]

        openai.api_key = api_key

        return api_key
