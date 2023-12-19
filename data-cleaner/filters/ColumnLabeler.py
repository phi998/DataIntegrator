import pandas as pd

from enums.chatgpt.Model import Model
from llm.chatgpt.chatgpt import ChatGPT
from sampler.table.FirstNRowsSampler import FirstNRowsSampler


class ColumnLabeler:

    def __init__(self, data_context):
        self.__data_context = data_context

    def label_columns(self, df, ontology=None):
        if ontology is None:
            ontology = []

        sampler = FirstNRowsSampler()  # use a better sampler
        sampled_rows = sampler.sample(df, 10)
        sampled_rows = sampled_rows.dropna(axis=1, how='all')

        sampled_rows = self.__delete_empty_columns(sampled_rows)
        # sampled_rows = self.__delete_low_entropy_columns(sampled_rows)

        print("Labelling columns...")

        columns_labels_dict = {}
        for i in sampled_rows.columns:
            print(f"Labeling column {i}")
            column_values = sampled_rows[i].tolist()
            try:
                column_name = self.__label_column(column_values, ontology)
            except Exception as e:
                print(f"Could not label column {i}, motivation: {e}")
                column_name = "Unknown"
            columns_labels_dict[int(i)] = column_name

        print(f"Got columns_labels_dict={columns_labels_dict}")

        columns_to_keep = list(columns_labels_dict.keys())
        columns_names = list(columns_labels_dict.values())

        df = df[[int(x) for x in columns_to_keep]]
        df.columns = columns_names

        return df

    def __label_column(self, column, ontology):
        chatgpt = ChatGPT(model=Model.GPT_3_5_16K)

        column = [str(col) for col in column if col is not None]

        ontology = ','.join(ontology)

        response = chatgpt.get_one_shot_solution(
            input_example='apple, banana, pear, pineapple',
            output_example='{"column_name":"fruit","confidence": 0.9}',
            task=f"You are an expert about {self.__data_context}",
            prompt="Answer the question based on the task below, If the question cannot be answered " \
                   "using the information provided answer with 'other'.\n" \
                   "Classify the column given to you into only one of these types that are separated with " \
                   f"comma: {ontology}",
            instructions=["Look at items values in detail",
                          "Select a class that best represents the meaning of all items",
                          "For the selected class write the confidence you would give in the interval [0,1]",
                          "Answer with only a json string like {'column_name':'effective name','confidence':confidence}, no prose"],
            prompt_input=';'.join(column)
        )

        return response["column_name"]

    def __delete_empty_columns(self, df):
        empty_columns = [col for col in df.columns if
                         df[col].apply(lambda x: x is None or (isinstance(x, str) and x.strip() == '')).all()]

        df = df.drop(empty_columns, axis=1)
        return df

    def __delete_low_entropy_columns(self, df):
        columns_to_drop = df.columns[(df.nunique() == 1) | (df.apply(lambda col: pd.isna(col).all()))]
        df = df.drop(columns=columns_to_drop)

        return df
