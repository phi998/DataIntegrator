import re

import numpy as np

'''
This class represents a filter used to delete columsn in a dataframe which present a certain % of empty cells
'''


class EmptyColumnsCleaner:

    def __init__(self, min_threshold):
        self.min_threshold = min_threshold

    def clean(self, df):
        return self.__remove_empty_columns(df=df)

    def __remove_empty_columns1(self, df):
        threshold = self.min_threshold * len(df)

        df = df.map(lambda x: np.nan if isinstance(x, str) and len(str(x)) > 0 and x.isspace() else x)
        df = df.map(lambda x: np.nan if re.match(r'^\\+$', str(x)) else x)
        df = df.fillna({col: '' for col in df.select_dtypes(include='object').columns})

        # pay attention to the difference between nan and ''
        df = df.dropna(axis=1, thresh=threshold)
        columns_to_remove = [col for col in df.columns if
                             self.__non_empty_values_ratio(df, col) <= self.min_threshold and df[col].dtype == 'object']
        df = df.drop(columns=columns_to_remove)

        return df

    def __remove_empty_columns(self, df):
        threshold = self.min_threshold * len(df)

        # operate on numeric columns at first
        df = df.dropna(axis=1, thresh=threshold)

        # operate on object columns
        columns_to_remove = [col for col in df.columns if
                             self.__non_empty_values_ratio(df, col) > self.min_threshold and df[col].dtype == 'object']
        df = df.drop(columns=columns_to_remove)

        return df

    def __remove_whitespace_and_newlines(self, s):
        formatted = re.sub(r'^\s+|\s+$', '', str(s), flags=re.MULTILINE)
        return formatted == ''

    def __non_empty_values_ratio(self, df, column_name):
        empty_string_count = df[column_name].apply(self.__remove_whitespace_and_newlines).sum()
        total_count = len(df)
        empty_string_ratio = empty_string_count / total_count

        return empty_string_ratio

    # TODO
    def __remove_low_entropy_columns(self, df, min_entropy):

        pass