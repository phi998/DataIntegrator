import numpy as np
from bs4 import BeautifulSoup
import pandas as pd


class HtmlCleaner:

    def clean(self, df):
        df = df.applymap(self.__clean_html_tags_nonull)
        return df

    def __clean_html_tags_nonull(self, text):
        if pd.notna(text):
            return self.__clean_html_tags(text)
        else:
            return np.nan

    def __clean_html_tags(self, text):
        soup = BeautifulSoup(str(text), 'html.parser')
        result = soup.get_text()

        return result

