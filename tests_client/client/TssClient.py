import json
import re

import requests


class TssClient:

    def create_table(self, table_name):
        endpoint = "http://localhost:8080/structures"

        collection_name = table_name
        cols_dict = {
        }

        data = {
            "collectionName": collection_name,
            "colName2type": cols_dict
        }

        response = requests.post(endpoint, json=data)
        return json.loads(response.text)

    def add_columns(self, table_name, colnames2type_dict):
        endpoint = "http://localhost:8080/structures"

        collection_name = table_name
        cols_dict = colnames2type_dict

        data = {
            "collectionName": collection_name,
            "colName2type": cols_dict
        }

        response = requests.put(endpoint, json=data)
        return json.loads(response.text)

    def upload_table(self, category, table_name, df):
        endpoint = "http://localhost:8080/tables"

        df.columns = [re.sub(r'\.\d+', '', col) for col in df.columns]
        df.columns = df.columns.str.lower()
        df_csv = df.to_csv(encoding="utf-8", index=False)

        fields = []
        # fields are automatically computed server side
        '''for col in df.columns:
            fields.append({
                "name": col,
                "symbolicName": "",  #server-side computed
                "type": "String"  #FIXME adapt other data types
            })'''

        data = {
            "tableName": table_name,
            "category": category,
            "content": df_csv,
            "fields": fields,
            "hasHeader": True
        }

        response = requests.post(endpoint, json=data)
        # return json.loads(response.text)
