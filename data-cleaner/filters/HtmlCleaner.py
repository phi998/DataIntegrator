import re

from bs4 import BeautifulSoup
import pandas as pd

'''
This class represents a filter that is intended to delete all html tags in a string for each cell in the dataframe
'''

class HtmlCleaner:

    def clean(self, df):
        df = df.map(self.__clean_html_tags_nonull)

        object_columns = df.select_dtypes(include='object').columns
        for column in object_columns:
            df[column] = df[column].apply(self.__remove_css_from_cell)

        return df

    def __clean_html_tags_nonull(self, text):
        if pd.notna(text):
            return self.__clean_html_tags(text)
        else:
            return ''

    def __clean_html_tags(self, text):
        soup = BeautifulSoup(str(text), 'html.parser')
        result = soup.get_text()

        return result

    def __remove_css_from_cell(self, cell_value):
        css_pattern = r'\b(.*?){.*?}'
        return re.sub(css_pattern, '', str(cell_value))

