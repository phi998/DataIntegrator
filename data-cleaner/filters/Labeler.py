import json

from enums.chatgpt.Model import Model
from llm.chatgpt.chatgpt import ChatGPT
from sampler.table.TokenLimitSampler import TokenLimitSampler


class Labeler:

    def __init__(self, data_context):
        self.data_context = data_context

    def label_columns(self, df, ontology=None):
        if ontology is None:
            ontology = []

        sampler = TokenLimitSampler(2600)
        sampled_rows = sampler.sample(df)

        sampled_rows = sampled_rows.dropna(axis=1, how='all')
        empty_columns = [col for col in sampled_rows.columns if
                         sampled_rows[col].apply(lambda x: x is None or (isinstance(x, str) and x.strip() == '')).all()]

        sampled_rows = sampled_rows.drop(empty_columns, axis=1)  # FIXME will it side effect original dataset??

        print("Labelling columns...")

        columns_labels_dict = self.__get_label_columns(sampled_rows, ontology)

        print(f"Got columns_labels_dict={columns_labels_dict}")

        columns_to_keep = list(columns_labels_dict.keys())
        columns_names = list(columns_labels_dict.values())

        df = df[[int(x) for x in columns_to_keep]]
        df.columns = columns_names

        return df

    def __get_label_columns(self, df, ontology):
        chatgpt = ChatGPT(model=Model.GPT_3_5_16K)

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

        response = {key: value for key, value in response.items() if value != 'other'}

        return response

    def __transform_df_to_input_string(self, df):
        df_dict = {}

        for column_name in df.columns:
            df_dict[column_name] = df[column_name].tolist()

        return json.dumps(df_dict)
