import json

from llm.chatgpt.chatgpt import ChatGPT
from sampler.FirstRowSampler import FirstRowSampler

'''

https://arxiv.org/pdf/2306.00745.pdf
'''


class Labeler:

    def __init__(self, data_context):
        self.data_context = data_context

    def label_columns(self, df):
        sampler = FirstRowSampler()
        sample = sampler.sample(df)

        df.columns = self.__get_columns_names(sample)
        return df

    def __get_columns_names(self, df):
        first_row_cells = df.values

        chatgpt = ChatGPT()
        task = f"You are an expert about {self.data_context}." if self.data_context is not None else ""
        prompt = f"You have to classify the columns of a given table with only one class, for example: " \
                 f"description, name, location, price, ..."
        instructions = [
            "Look at the input given to you and make a table out of it",
            "Look at the cell values in detail",
            "For each column select a class that best represents the meaning of all cells in the column",
            "Answer with the selected class for each columns with the json format {Column1:class, Column2:class, ...}"
            "Answer with only the string associated to the class. No prose"
        ]
        example_in = "Classify these table columns: Column 1 || Column 2 || Column 3 || Column 4\n" \
                     "Friends Pizza || 2525 || Cash Visa MasterCard || 7:30AM ||\n"
        example_out = "Column 1: Name of restaurant\nColumn 2: postal code\nColumn 3: payment accepted\nColumn 4: time"
        prompt_input = self.__build_prompt_input(prompt_instruction="Classify these table columns: ", first_row_cells=first_row_cells)

        column_names = chatgpt.get_one_shot_solution(input_example=example_in, output_example=example_out, task=task,
                                                    prompt=prompt, prompt_input=prompt_input,
                                                    instructions=instructions)

        return json.loads(column_names).values()

    def __get_columns_names_one_by_one(self, df):
        json_data = []
        column_names = []

        for column in df.columns:
            if df[column].to_list() is not None:
                json_data.append(df[column].to_list())
                colname = self.__get_column_name(df[column].to_list())
                column_names.append(colname)

        return column_names

    def __build_prompt_input(self, prompt_instruction, first_row_cells):
        prompt = prompt_instruction

        i = 1
        for cell in first_row_cells:
            prompt += f'Column {i}: {cell} ||'
            i = i+1

        prompt = prompt[:-3]

        return prompt
