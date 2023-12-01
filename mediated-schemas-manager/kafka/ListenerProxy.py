import json
from io import StringIO

import pandas as pd

from chain.DefaultChain import DefaultChain
from proxy.FileStorageApi import FileStorageApi


class ListenerProxy:

    def __init__(self):
        self.modality = "DEBUG"
        self.dis_endpoint = "http://dis:8080"

    def align_tables(self, data_cleaned_event):
        data_cleaned_event = json.loads(data_cleaned_event)

        job_id = data_cleaned_event["jobId"]
        job_name = data_cleaned_event["jobName"]
        job_has_ontology = data_cleaned_event["hasOntology"]
        job_tables = data_cleaned_event["data"]["tables"]

        dfs = []

        fs = FileStorageApi()

        for table in job_tables:
            table_name = table["tableName"]
            table_resource_url = table["resourceUrl"]

            table_content = fs.get_file(table_resource_url)

            csv_file = StringIO(table_content)
            df = pd.read_csv(csv_file, encoding="utf-8")

            dfs.append(df)

        chain = DefaultChain(context=job_name)

        final_df = chain.apply(dfs=dfs, ontology_present=job_has_ontology)

        # save schema to dis
        table_content = final_df.to_csv(index=False)
        uploaded_file_response = fs.upload_file(job_name, table_content)
        resource_url = uploaded_file_response["resourceUrl"]

        data_aligned_event = {
            "jobId": job_id, "jobName": job_name, "jobResultResourceUrls": [resource_url]
        }

        return data_aligned_event
