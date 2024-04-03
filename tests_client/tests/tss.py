import unittest

import requests
import csv


class TssClientTest(unittest.TestCase):

    def test_create_table_structure(self):
        endpoint = "http://localhost:8082/structures"

        collection_name = "zalando"
        cols_dict = {
        }

        data = {
            "collectionName": collection_name,
            "colName2type": cols_dict
        }

        response = requests.post(endpoint, json=data)

        self.assertEqual(response.status_code, 200)

    def test_alter_table(self):
        endpoint = "http://localhost:8082/structures"

        collection_name = "zalando"
        cols_dict = {
            "Product Name": "string",
            "Brand Name": "string",
            "Features": "string"
        }

        data = {
            "collectionName": collection_name,
            "colName2type": cols_dict
        }

        response = requests.put(endpoint, json=data)

        self.assertEqual(response.status_code, 200)


    def test_upload_table(self):
        endpoint = "http://localhost:8082/tables"

        with open("datasets/zalando.csv", "r", encoding='utf-8') as f:
            csv_data = f.read()

        data = {
            "table_name": "zalando",
            "category": "zalando",
            "content": csv_data,
            "fields": [
                {"name": "Product Name", "symbolicName": "product_name", "type": "String"},
                {"name": "Brand Name", "symbolicName": "brand_name", "type": "String"},
                {"name": "Features", "symbolicName": "features", "type": "String"}
            ],
            "hasHeader": "true"
        }

        response = requests.post(endpoint, json=data)

        self.assertEqual(response.status_code, 200)

    def test_query(self):
        endpoint = "http://localhost:8082/query"

        params = {"book_title": " ", "n": 2, "collectionName": "books"}

        response = requests.get(endpoint, params=params)

        if response.status_code == 200:
            print(response.json())
        else:
            print(f"Error: {response.status_code}")
            print(response.text)

        self.assertEqual(response.status_code, 200)
