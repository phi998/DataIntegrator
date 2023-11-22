from unittest import TestCase

import pandas as pd

from filters.sub.CouplesLabeler import CouplesLabeler


class TestLabeler(TestCase):

    def test_companiesminisample(self):
        self.__run_experiment(name="companies2_minisample", context="companies", important_attributes=["Name"])

    def __run_experiment(self, name, context, important_attributes):
        file_path = f"datasets/input/to_label/{name}.csv"
        couples_df = pd.read_csv(file_path, encoding="ISO-8859-1")

        couples_labeler = CouplesLabeler()
        labeled_df = couples_labeler.label_couples(couples_df, important_attributes, context)

        output_file_path = f"datasets/output/labeled/{name}.csv"
        labeled_df.to_csv(output_file_path, index=False)
