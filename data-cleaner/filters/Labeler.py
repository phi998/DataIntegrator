import json

from enums.chatgpt.Model import Model
from llm.chatgpt.chatgpt import ChatGPT
from sampler.Sampler import Sampler


class Labeler:

    def __init__(self, data_context=None):
        self.data_context = data_context

    def label_columns(self, df):
        sampler = Sampler()
        sample = sampler.sample(df)

        df.columns = self.__get_column_names(sample)
        return df

    def __get_column_names(self, df):
        json_data = []

        for column in df.columns:
            if df[column].to_list() is not None:
                json_data.append(df[column].to_list())

        num_columns = df.shape[1]

        chatgpt = ChatGPT()
        task = f"You are an expert about {self.data_context}" if self.data_context is not None else ""
        prompt = f"I'm going to give you a list of lists in json format. " \
                 f"Give me a list of names of {num_columns} elements that best represents each of them. For example if i have" \
                 f"this list '[[apple,  pear, cherry, banana],[ferrari,fiat,mercedes]]' the output is '[fruit,cars]'." \
                 f"If the list has only blank elements answer with none. Names in the list can be repeated." \
                 f"No prose, output format: json list. {json_data}"

        # TODO with 1-shot
        column_names = chatgpt.get_simple_solution(task=task, prompt=prompt, model=Model.GPT_3_5)

        return json.loads(column_names)
