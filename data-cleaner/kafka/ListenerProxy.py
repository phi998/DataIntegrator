from io import StringIO

import pandas as pd

from chains.DefaultChain import DefaultChain
from stats.FilePrinter import FilePrinter


class ListenerProxy:

    def __init__(self):
        self.modality = "DEBUG"

    def clean_tables(self, job):
        context = job["jobName"]
        ontology = job["data"]["ontology"]
        tables = job["data"]["tables"]

        data_cleaned_event = {}
        data_cleaned_event["jobId"] = job["jobId"]
        data_cleaned_event["jobName"] = job["jobName"]
        data_cleaned_event["data"] = {}
        data_cleaned_event["data"]["tables"] = []

        chain = DefaultChain(context, ontology)

        for table in tables:
            table_name = table["tableName"]
            table_content = table["tableContent"]

            csv_file = StringIO(table_content)
            df = pd.read_csv(csv_file, encoding="utf-8", header=None)
            df_cleaned = chain.apply(df)

            table_data_structured = {
                "tableName": table_name,
                "tableContent": df_cleaned.to_csv(index=False)
                }
            data_cleaned_event["data"]["tables"].append(table_data_structured)

            if self.modality == "DEBUG":
                fp = FilePrinter()
                fp.print_table_to_csv(table_name=table_data_structured["tableName"], table_content=table_data_structured["tableContent"])

        return data_cleaned_event
