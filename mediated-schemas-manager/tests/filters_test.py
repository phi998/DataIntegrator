import os
import unittest

import pandas as pd

from chain.DefaultChain import DefaultChain
from filters.SchemaAligner import SchemaAligner


class FiltersTest(unittest.TestCase):

    def test_jobs(self):
        self.run_job(group_name="jobs", context="job advertisements")

    def test_jobs_a(self):
        self.run_job(group_name="jobs_a", context="job advertisements")

    def test_finance(self):
        self.run_job(group_name="finance", context="finance")

    def run_job(self, group_name, context):
        folder_path = f"datasets/input/labeled/{group_name}"

        files = os.listdir(folder_path)
        csv_files = [file for file in files if file.endswith('.csv')]

        dataframes = []

        for file in csv_files:
            file_path = os.path.join(folder_path, file)
            df = pd.read_csv(file_path)
            dataframes.append(df)

        chain = DefaultChain(context=context)
        result_df = chain.apply(dataframes, True)

        result_df.to_csv(f'datasets/output/{group_name}_.csv', index=False)

        '''i = 0
        for renamed_df in renamed_dfs:
            renamed_df.to_csv(f'datasets/output/{group_name}_{str(i)}.csv', index=False)
            i += 1'''
