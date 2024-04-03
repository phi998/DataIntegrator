import unittest
import pandas as pd
from io import StringIO

from filters.ColumnEntropyFilter import ColumnEntropyFilter
from filters.ColumnTrimFilter import ColumnTrimFilter
from filters.EmptyColumnsCleaner import EmptyColumnsCleaner
from filters.HtmlCleaner import HtmlCleaner


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

    def test_low_entropy_columns_filter(self):
        df = pd.read_csv('datasets/low_entropy/input.csv', header=None)

        table_column_trimmer = ColumnTrimFilter()
        df = table_column_trimmer.apply(df)

        entropy_filter = ColumnEntropyFilter(0.1)  # FIXME
        df = entropy_filter.apply(df, ontology=None)

        df.to_csv("datasets/low_entropy/output.csv", header=None)



