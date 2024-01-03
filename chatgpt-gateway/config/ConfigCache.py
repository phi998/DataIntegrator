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
