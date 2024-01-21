import json
import os


class ConfigCache:
    _instance = None

    def __init__(self):
        if not ConfigCache._instance:
            ConfigCache._instance = self
            self.__dict_file_path = self.__get_config_file_path()
            self.__configs = {}

            self.__init_configurations()

    @staticmethod
    def get_instance():
        if not ConfigCache._instance:
            ConfigCache._instance = ConfigCache()
        return ConfigCache._instance

    def __init_configurations(self):
        with open(self.__dict_file_path, 'r') as file:
            data_dict = json.load(file)

        self.__configs = data_dict

    def __get_config_file_path(self):
        config_path = os.path.dirname(os.path.abspath(__file__)) + "/config.json"
        return config_path

    def get_configuration(self, key):
        return self.__configs[key]

    def get_prompt_examples(self):

        examples = [
              {
                "input": "apple, banana, pear, pineapple",
                "output": "{\"column_name\":\"fruit\",\"confidence\":0.9}"
              },
              {
                "input": "John, Emily, Alex, Sarah, Michael",
                "output": "{\"column_name\":\"name\",\"confidence\":0.9}"
              },
              {
                "input": "8kg, 12kg, 90kg, 45g, 1ton",
                "output": "{\"column_name\":\"weight\",\"confidence\":0.9}"
              },
              {
                "input": "Rome, Paris, London, New York, Beijing",
                "output": "{\"column_name\":\"city\",\"confidence\":0.9}"
              },
              {
                "input": "hp, lenovo, lg, samsung, apple, microsoft",
                "output": "{\"column_name\":\"company name\",\"confidence\":0.9}"
              }
            ]

        num_of_shots = self.__configs["shots"]

        return examples[:num_of_shots]
