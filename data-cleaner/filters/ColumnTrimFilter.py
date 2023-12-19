

class ColumnTrimFilter:

    def apply(self, df):
        df_cleaned = df.map(self.__strip_whitespace)
        return df_cleaned

    def __strip_whitespace(self, element):
        if isinstance(element, str):
            return element.strip()
        else:
            return element
