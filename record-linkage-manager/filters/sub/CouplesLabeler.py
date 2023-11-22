
'''
This class labels the couples in order to get the final dataset to give to deepmatcher
'''
import json

import pandas as pd

from llm.chatgpt.chatgpt import ChatGPT
from llm.chatgpt.chatgpt_utils import ChatGptUtils


class CouplesLabeler:

    def label_couples(self, couples_df, important_attributes, context):

        chatgpt = ChatGPT()
        important_attributes_lr = self.__transform_list(important_attributes)
        is_match = []

        sub_dfs = self.__split_df(couples_df, 1000)
        for sub_df in sub_dfs:
            sub_df_filtered = sub_df.loc[:, important_attributes_lr]
            json_llm_input = self.__couples_to_json_string(sub_df_filtered)

            solution = chatgpt.get_one_shot_solution(
                task=context,
                prompt="Classify (in terms of probability) each couple if it is in match or not",
                instructions=[
                    "For each couple, give your confidentiality, between 0 and 1",
                    "For each couple report an output of the form {couple_id:probability}",
                    "I want the probability for all the couples",
                    "Do not omit any couple",
                    "IMPORTANT: Output must be only the json code, NO PROSE!"
                ],
                prompt_input=json_llm_input,
                input_example="{'0':['DYMO D1 19 mm x 7 m','Dymo D1 (19mm x 7m - BoW)'],'1':['Dymo D1 19mm x 7m','DYMO D1 Tape 24mm']}",
                output_example="{'0':0.9,'1':0.1}"
            )

            probabilities = list(solution.values())

            is_match = is_match + probabilities

        couples_df["matching_prob"] = is_match

        return couples_df

    def __transform_list(self, input_list):
        transformed_list = [f'{item}_l' for item in input_list] + [f'{item}_r' for item in input_list]
        return transformed_list

    def __split_df(self, df, max_sample_tokens_length):
        result_dataframes = []
        current_tokens = 0
        current_dataframe = pd.DataFrame(columns=df.columns)
        current_dataframe["TokenCount"] = 0

        aux_columns = ["distance", "table_id_l", "table_id_r"]
        columns_to_consider_for_tokens = [element for element in df.columns.tolist() if element not in aux_columns]

        df['TokenCount'] = df.apply(self.__calculate_total_token_count, args=(columns_to_consider_for_tokens,), axis=1)

        for index, row in df.iterrows():
            i = index
            r = row
            if current_tokens + row['TokenCount'] <= max_sample_tokens_length:
                current_tokens += row['TokenCount']
                current_dataframe = pd.concat([current_dataframe, pd.DataFrame([row])], ignore_index=True)
            else:
                current_dataframe = current_dataframe.drop('TokenCount', axis=1)
                result_dataframes.append(current_dataframe.copy())
                current_dataframe = pd.DataFrame(columns=df.columns)
                current_tokens = 0

        if not current_dataframe.empty:
            current_dataframe = current_dataframe.drop('TokenCount', axis=1)
            result_dataframes.append(current_dataframe)

        df.drop('TokenCount', axis=1, inplace=True)

        return result_dataframes

    def __calculate_total_token_count(self, row, columns):
        total_tokens = 0
        for column in columns:
            tokens = ChatGptUtils.count_tokens(text=row[column])
            total_tokens += tokens
        return total_tokens

    def __couples_to_json_string(self, couples_df):
        return str(self.__couples_to_json(couples_df))

    def __couples_to_json(self, couples_df):
        llm_input = {}

        for index, row in couples_df.iterrows():
            llm_input[index] = []
            for column in couples_df.columns:
                llm_input[index].append(row[column])

        return llm_input
