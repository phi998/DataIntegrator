
'''
The class has the purpose to return
'''
import json

from llm.chatgpt.chatgpt import ChatGPT
from sampler.TokenLimitSampler import TokenLimitSampler


class Labeler:

    def __init__(self, data_context):
        self.data_context = data_context

    def label_columns(self, df, ontology=None):
        if ontology is None:
            ontology = []

        sampler = TokenLimitSampler(3000)
        sampled_rows = sampler.sample(df)

        sampled_rows = sampled_rows.dropna(axis=1, how='all')
        empty_columns = [col for col in sampled_rows.columns if
                         sampled_rows[col].apply(lambda x: x is None or (isinstance(x, str) and x.strip() == '')).all()]

        sampled_rows = sampled_rows.drop(empty_columns, axis=1)

        return self.__label_columns(sampled_rows, ontology)


    def __label_columns(self, df, ontology):
        chatgpt = ChatGPT()

        input_json = self.__transform_df_to_input_string(df)

        ontology_string = ', '.join(ontology)

        response = chatgpt.get_one_shot_solution(
            input_example="{'0':['apple','banana','apple','pear'],'1':['AAA','hi','flo','car'],'2':['dog','cat','bird','mouse']}",
            output_example="{0:'fruit',2:'animals'}",
            task=f"You are an expert about {self.data_context}",
            prompt=f"I give you a json dictionary that represents a table, each index identifies a column "
                   f"and each value represents the column cells values.\n"
                   f"Classify the column with one of the following classes that are separated with comma: "
                   f"{ontology_string}",
            instructions=["Look at the cell values in detail",
                          "For each column, select a class that best represents the meaning of all cells in the column",
                          "Answer with the selected class for each column with the json format as shown in the example",
                          "If the column doesn't belong to any of the given classes, mark it as 'other'",
                          "IMPORTANT: No prose in the answer!"
                          ],
            prompt_input=f"{input_json}"
        )

        columns_to_keep = list(response.keys())
        columns_names = list(response.values())

        df = df[[int(x) for x in columns_to_keep]]
        df.columns = columns_names

        return df

    def __transform_df_to_input_string(self, df):
        df_dict = {}

        i = 0
        for column_name in df.columns:
            df_dict[i] = df[column_name].tolist()
            i += 1

        return json.dumps(df_dict)
