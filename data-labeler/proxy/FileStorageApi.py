import base64
import json

import requests


class FileStorageApi:

    def __init__(self):
        self.dis_endpoint = "http://dis:8080"

    def get_file(self, resource_url):
        print(f"Fetching table data from resource url: {resource_url}")
        response = requests.get(resource_url)
        response.raise_for_status()

        data = {}

        if response.status_code == 200:
            data = response.json()
            print(f"Fetched data from {resource_url} successfully")
        else:
            print(f"Error: {response.status_code}")
            print(response.text)

        file_content = data["fileContent"]
        file_content = base64.b64decode(file_content).decode('utf-8')

        # print(f"Retrieved file {file_content}")

        return file_content

    def upload_file(self, file_name, file_content):
        print(f"Uploading file {file_name}")

        dis_url = self.dis_endpoint + "/files"

        base64_encoded_content = base64.b64encode(file_content.encode('utf-8')).decode('utf-8')

        upload_body = {
            "fileName": file_name,
            "fileContent": base64_encoded_content,
            "persist": True
        }

        response = requests.post(dis_url, json=upload_body)

        if response.status_code == 200:
            print('POST request successful!')
            print('Response:', response.text)
        else:
            print(f'Error in POST request. Status code: {response.status_code}')
            print('Response:', response.text)

        return json.loads(response.text)
