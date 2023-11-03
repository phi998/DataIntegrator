import json
import logging

from llm.chatgpt.chatgpt import ChatGPT
from sampler.Sampler import Sampler


class UsefulDataFilter:

    def clean(self, df):
        sampler = Sampler()
        sampled_rows = sampler.sample(df)

        columns_to_drop = self.__get_useless_columns_indexes(sampled_rows)
        df = df.drop(df.columns[columns_to_drop], axis=1)

        return df

    def __get_useless_columns_indexes(self, df):
        csv_data = df.to_csv(index=False, header=False)

        chatgpt = ChatGPT()
        prompt = f"I give some rows of a table in csv format." \
                 f"Return me the indexes of columns containing useful data in json list format, key name must be indexes?" \
                 f"Choose data that may make sense to you to be included in a database." \
                 f"I want only the json code of one list, no prose!  {csv_data}"
        solution = chatgpt.get_solution(task='', prompt=prompt)

        response = solution.choices[0].message.content

        logging.debug(f"UsefulDataFilter: response={response}")

        indexes = json.loads(response)

        num_columns = len(df.columns)
        columns_indexes = list(range(num_columns))
        result = [x for x in columns_indexes if x not in indexes["indexes"]]

        return result
