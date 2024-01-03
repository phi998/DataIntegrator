from io import StringIO

import pandas as pd

from FileStorageApi import FileStorageApi
from chains.DefaultChain import DefaultChain


class JobWorker:

    def __init__(self):
        self.modality = "DEBUG"
        self.dis_endpoint = "http://dis:8080"

    def clean_tables(self, job):
        context = job["jobName"]
        ontology = job["data"]["ontology"]
        tables = job["data"]["tables"]

        chain = DefaultChain(context, ontology)

        cleaned_tables = []

        for table in tables:
            table_name = table["tableName"]
            table_content_resource_url = table["tableUrl"]

            file_storage_api = FileStorageApi()
            table_content = file_storage_api.get_file(table_content_resource_url)

            csv_file = StringIO(table_content)
            df = pd.read_csv(csv_file, encoding="utf-8", header=None)
            print(f"Applying chain to table {table_name}")
            df_cleaned = chain.apply(df)
            print(f"Uploading table {table_name}")
            cleaned_tables.append({
                "tableName": table_name,
                "df": df_cleaned
            })

        return cleaned_tables


