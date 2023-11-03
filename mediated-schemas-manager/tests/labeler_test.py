import unittest
import pandas as pd

from column.labeler import Labeler


class LabelerTest(unittest.TestCase):

    def test_label_columns_almalaurea(self):
        labeler = Labeler()
        df = pd.read_csv("datasets/input/labeling/sample_almalaurea.csv")

        df = labeler.label_columns(df)

        df.to_csv("datasets/output/labeling/out_sample_almalaurea.csv", sep=',', encoding='utf-8')


    def test_label_columns_glassdoor(self):
        labeler = Labeler()
        df = pd.read_csv("datasets/input/labeling/sample_glassdoor.csv")

        df = labeler.label_columns(df)

        df.to_csv("datasets/output/labeling/out_sample_glassdoor.csv", sep=',', encoding='utf-8')
