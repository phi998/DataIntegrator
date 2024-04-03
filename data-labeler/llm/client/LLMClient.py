import json

import requests


class LLMClient:

    def __init__(self):
        self.__api_base_url = "http://chatgpt:8083"

    def chat(self, task, prompt, prompt_input, instructions, examples, observations):
        url = self.__api_base_url + "/chat"

        json_data = {
            "task": task,
            "prompt": prompt,
            "prompt_input": prompt_input,
            "instructions": instructions,
            "observations": observations,
            "examples": examples
        }

        response = requests.post(url, json=json_data, timeout=20)

        if response.status_code == 200:
            print('POST request successful!')
            print('Response:', response.text)
        else:
            print(f'POST request failed with status code {response.status_code}')
            print('Response:', response.text)

        return json.loads(response.text)
