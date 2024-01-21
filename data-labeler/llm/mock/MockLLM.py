import json
import os

from enums.chatgpt.Model import Model
from llm.GenericLLMApi import GenericLLMApi


class MockLLM(GenericLLMApi):

    '''
    :param prompt is the table group name for which i want the aligned schema
    '''
    def get_simple_solution(self, model=Model.NULL, task="", prompt=""):
        return self.__get_response(group_name=prompt)

    def __get_simple_solution(self, group_name):
        ROOT_DIR = os.path.dirname(
            os.path.abspath(__file__)
        )

        # FIXME
        file_location = f"C:\\Users\\fippi\\DEV\\projects\\DataIntegrator\\mediated-schemas-manager\\tests\\datasets\\mock\\llm\\{group_name}.json"

        with open(file_location) as json_file:
            mock_dict = json.load(json_file)

        return mock_dict

    def get_one_shot_solution(self, examples, input_example, output_example, task, prompt, prompt_input, instructions,
                              model=Model.DEFAULT):
        return json.loads("{}")
