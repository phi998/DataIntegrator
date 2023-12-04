import re

import numpy as np

class NumericColumnsFilter:

    def clean(self, df):
        return self.__clean_numeric_columns(df)

    def __clean_numeric_columns(self, df):
        columns_to_delete = [col for col in df.columns if self.__should_delete_column(df[col])]
        return df.drop(columns=columns_to_delete)

    def __should_delete_column(self, column):
        if np.issubdtype(column.dtype, np.number):
            return True
        elif column.apply(lambda x: self.__remove_non_alphanumeric(str(x)).isnumeric()).all():
            return True
        return False

    def __remove_non_alphanumeric(self, input_string):
        return re.sub(r'[^a-zA-Z0-9]', '', input_string)
