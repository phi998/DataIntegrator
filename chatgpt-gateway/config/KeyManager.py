import json
import os
from os.path import dirname, join

import openai


class KeyManager:

    def get_key(self):
        api_key = os.getenv("OPENAI_KEY")
        if api_key is not None:
            return api_key

        project_root = dirname(dirname(__file__))
        output_path = join(project_root, 'config/')

        with open(output_path + "/secrets.json") as f:
            secrets = json.load(f)
            api_key = secrets["api_key"]

        return api_key
