import json

from enums.chatgpt.Model import Model
from llm.chatgpt.chatgpt import ChatGPT
from sampler.table.TokenLimitSampler import TokenLimitSampler


class UsefulDataFilter:

    def __init__(self, data_context):
        self.data_context = data_context
        self.__chatgpt = ChatGPT(Model.GPT_3_5)

    def clean(self, df):
        sampler = TokenLimitSampler(3000)
        sampled_rows = sampler.sample(df)

        sampled_rows = sampled_rows.dropna(axis=1, how='all')
        empty_columns = [col for col in sampled_rows.columns if sampled_rows[col].apply(lambda x: x is None or (isinstance(x, str) and x.strip() == '')).all()]

        sampled_rows = sampled_rows.drop(empty_columns, axis=1)

        columns_to_drop = self.__get_useless_columns_indexes(sampled_rows)
        df = df.drop(df.columns[columns_to_drop], axis=1)

        return df

    def __get_useless_columns_indexes(self, df):
        column_dict = {}
        for col_index, col_name in enumerate(df.columns):
            column_dict[col_index] = df[col_name].tolist()

        json_string = json.dumps(column_dict)

        task = f"You are an expert about {self.data_context}." if self.data_context is not None else ""
        prompt = f"I'm going to give you a json dictionary, return in a json integer list the keys you would classify as 'useful'." \
                 f"Important instruction: don't write any prose about it! I want just the json list in the answer!\n" \
                 f"The json dictionary is the following:\n" \
                 f"{json_string}"


        indexes = self.__get_indexes_solution(task=task, prompt=prompt)

        num_columns = len(df.columns)
        columns_indexes = list(range(num_columns))
        result = [x for x in columns_indexes if x not in indexes]

        return result

    def __get_indexes_solution(self, task, prompt):
        max_retries = 3
        current = 0
        success = False
        indexes = []

        while current < max_retries and not success:
            try:
                solution = self.__chatgpt.get_simple_solution(task=task, prompt=prompt)
                indexes = [int(x) for x in solution]
                success = True
            except Exception as e:
                print(f"Error occurred: {e}")
                current += 1

        return indexes
