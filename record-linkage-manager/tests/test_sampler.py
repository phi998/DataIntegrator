import os
import unittest

import pandas as pd

from filters.sub.CouplesSampler import CouplesSampler


class FiltersTest(unittest.TestCase):

    def test_jobs(self):
        important_attributes = ["Company Names"]
        self.run_job("jobs", important_attributes)

    def test_companies(self):
        important_attributes = ["Name"]
        self.run_job("companies", important_attributes)

    def test_companies2(self):
        important_attributes = ["Name"]
        self.run_job("companies2", important_attributes)

    def run_job(self, group_name, important_attributes):
        folder_path_in = f"datasets/input/{group_name}"
        folder_path_out = f"datasets/output/{group_name}"

        files = os.listdir(folder_path_in)
        csv_files = [file for file in files if file.endswith('.csv')]

        dataframes = []

        for file in csv_files:
            file_path = os.path.join(folder_path_in, file)
            df = pd.read_csv(file_path, encoding="ISO-8859-1")
            dataframes.append(df)

        couples_sample = CouplesSampler(500)
        sampled_couples_df = couples_sample.sample_couples(dataframes, important_attributes)


        # stabilire criterio per scelta campioni (per esempio sulla base della distanza)

        # sottoponi coppie a chatgpt


        # Save to output
        sampled_couples_df.to_csv(folder_path_out + ".csv", index=False)


