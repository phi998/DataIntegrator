import unittest
import pandas as pd
from io import StringIO

from filters.ColumnLabeler import ColumnLabeler
from filters.EmptyColumnsCleaner import EmptyColumnsCleaner
from filters.HtmlCleaner import HtmlCleaner
from filters.Labeler import Labeler
from filters.UsefulDataFilter import UsefulDataFilter


class FiltersTest(unittest.TestCase):

    def test_html_cleaner(self):
        html_cleaner = HtmlCleaner()
        df = pd.read_csv('datasets/test.csv', header=None)
        df = html_cleaner.clean(df)

        csv_string = StringIO()
        df.to_csv(csv_string, index=False, header=None)
        csv_data = csv_string.getvalue()

        self.assertEqual(",data,nan,value, \r\n,hello,world,nan, \r\n", csv_data)

        csv_string.close()

    def test_empty_columns_cleaner(self):
        empty_columns_cleaner = EmptyColumnsCleaner(0.4)
        df = pd.read_csv('datasets/test.csv', header=None)
        df = empty_columns_cleaner.clean(df)

        csv_string = StringIO()
        df.to_csv(csv_string, index=False, header=None)
        csv_data = csv_string.getvalue()

        self.assertEqual("<html>,data,,value\r\n<html>,hello,world,\r\n", csv_data)

        csv_string.close()

    def test_useful_columns_filter(self):
        df = pd.read_csv('datasets/useful_columns_filter/input.csv', header=None)

        cleaner = UsefulDataFilter()
        df = cleaner.clean(df)

        df.to_csv("datasets/useful_columns_filter/output.csv", header=None)

    def test_labeler(self):
        df = pd.read_csv('datasets/labeler/input.csv', header=None)

        labeler = Labeler("job advertisement")
        df = labeler.label_columns(df, ["Company name", "Role", "Location", "Description"])

        df.to_csv("datasets/labeler/output.csv")

    def test_column_labeler(self):
        df = pd.read_csv('datasets/labeler/input.csv', header=None)

        c_labeler = ColumnLabeler("job advertisement")
        df = c_labeler.label_columns(df, ["Company name", "Role", "Location", "Description", "Advertisement Content"])

        df.to_csv("datasets/c_labeler/output.csv")

