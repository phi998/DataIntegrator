import json
import os


class ConfigCache:
    _instance = None

    def __init__(self):
        if not ConfigCache._instance:
            ConfigCache._instance = self
            self.__dict_file_path = self.__get_config_file_path()
            self.__ontologies_examples_path = self.__get_ontologies_examples_path()
            self.__configs = {}
            self.__examples = {}

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

        with open(self.__ontologies_examples_path, 'r') as file:
            examples = json.load(file)

        self.__examples = examples

    def __get_config_file_path(self):
        config_path = os.path.dirname(os.path.abspath(__file__)) + "/config.json"
        return config_path

    def __get_ontologies_examples_path(self):
        config_path = os.path.dirname(os.path.abspath(__file__)) + "/examples.json"
        return config_path

    def get_configuration(self, key):
        return self.__configs[key]

    def get_prompt_examples(self, ontology_name):
        examples = self.__examples[ontology_name]

        num_of_shots = self.__configs["shots"]

        return examples[:num_of_shots]
