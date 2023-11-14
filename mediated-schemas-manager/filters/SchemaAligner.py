import pandas as pd

from llm.chatgpt.chatgpt import ChatGPT


class SchemaAligner:

    def align_schemas(self, dfs, context):
        mscols_2_sscols = self.__get_mediated_schema_structure(dfs, context)
        alignment_definition = self.__invert_key_vals_dict(mscols_2_sscols)

        mediated_schema_column_names = list(mscols_2_sscols.keys())

        mediated_schema = pd.DataFrame(columns=mediated_schema_column_names)

        for df in dfs:
            df = self.__rename_schema_columns(df, alignment_definition)
            df = df.loc[:, ~df.columns.duplicated()]
            df.reset_index(drop=True, inplace=True)
            mediated_schema = pd.concat([mediated_schema, df], join='outer', ignore_index=True)
            mediated_schema.reset_index(inplace=True, drop=True)

        return mediated_schema

    def __get_mediated_schema_structure(self, dfs, context):
        llm_input = {}

        i = 1
        for df in dfs:
            df_columns_list = df.columns.values
            llm_input["Table " + str(i)] = df_columns_list
            i += 1

        chatgpt = ChatGPT()
        gpt_response = chatgpt.get_one_shot_solution(
            input_example="Table 1: title, article content, date, author \n"
                          "Table 2: article title, content, date, author name \n"
                          "Table 3: title, content, author, location",
            output_example='{"title":["title","article title"],"content":["article content", "content"],'
                           '"author":["author","author name"],"date":["date"],"location": ["location"]}',
            task=f"You are an expert about {context}",
            prompt="I have different tables. For each of them I give you columns names and you have to define a mediated schema",
            instructions=[
                "for each column name, put it in a bucket related to the same meaning",
                "answer in json format, no prose",
                "do not forget any column name"
            ],
            prompt_input=str(llm_input)
        )

        return gpt_response

    def __single_schema_to_mediated_schema_column_name(self, ms_definition, single_schema_column_name):

        for key, value in ms_definition:
            mediated_schema_column_name = key
            column_names_list = value

            for column_name in column_names_list:
                if single_schema_column_name == column_name:
                    return mediated_schema_column_name


        raise Exception(f"No name found for column {single_schema_column_name}")

    def __rename_schema_columns(self, df, column_names_mapping):
        df.rename(columns=column_names_mapping, inplace=True)
        return df

    def __invert_key_vals_dict(self, original_dict):
        new_dict = {}
        for key, values in original_dict.items():
            for value in values:
                new_dict[value] = key
        return new_dict

