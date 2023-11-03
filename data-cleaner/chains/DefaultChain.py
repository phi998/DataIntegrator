import numpy as np

from filters.EmptyColumnsCleaner import EmptyColumnsCleaner
from filters.HtmlCleaner import HtmlCleaner
from filters.Labeler import Labeler
from filters.UsefulDataFilter import UsefulDataFilter


class DefaultChain:

    def apply(self, df):  # TODO Refactor
        # apply the chain
        df = df.iloc[:, 3:]

        html_cleaner = HtmlCleaner()
        df = html_cleaner.clean(df)

        empty_columns_cleaner = EmptyColumnsCleaner(0.4)
        df = empty_columns_cleaner.clean(df)

        useful_data_filter = UsefulDataFilter()
        df = useful_data_filter.clean(df)

        labeler = Labeler()
        df = labeler.label_columns(df)

        return df
