import os
import unittest

import pandas as pd

from filters.SchemaAligner import SchemaAligner


class FiltersTest(unittest.TestCase):

    def test_jobs(self):
        self.run_job(group_name="jobs", context="job advertisements")

    def test_finance(self):
        self.run_job(group_name="finance", context="finance")

    def run_job(self, group_name, context):
        folder_path = f"datasets/input/labeled/{group_name}"

        files = os.listdir(folder_path)

        # Filter only CSV files
        csv_files = [file for file in files if file.endswith('.csv')]

        # Initialize an empty dictionary to store dataframes
        dataframes = []

        # Loop through each CSV file and read it into a dataframe
        for file in csv_files:
            file_path = os.path.join(folder_path, file)
            dataframes.append(pd.read_csv(file_path))

        schema_aligner = SchemaAligner()
        mediated_schema = schema_aligner.align_schemas(dataframes, context)

        mediated_schema.to_csv(f'datasets/output/{group_name}.csv', index=False)
