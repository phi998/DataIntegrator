import pandas as pd


class ColumnsUrlEraser:

    def apply(self, df):
        df = df.map(self.__remove_urls)
        return df

    def __remove_urls(self, text):
        url_pattern = r'https?://\S+|www\.\S+'
        return ' '.join(pd.Series(text).str.replace(url_pattern, '').tolist())
