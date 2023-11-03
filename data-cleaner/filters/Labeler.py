import json
from llm.chatgpt.chatgpt import ChatGPT
from sampler.Sampler import Sampler


class Labeler:

    def label_columns(self, df):
        sampler = Sampler()
        df = sampler.sample(df)

        df.columns = self.__get_column_names(df)
        return df

    def __get_column_names(self, df):
        json_data = []
        for column in df.columns:
            json_data.append(df[column].to_list())

        num_columns = df.shape[1]

        chatgpt = ChatGPT()
        prompt = f"I'm going to give you a list of lists in json format." \
                 f"Give me a list of names of {num_columns} elements that best represents each of them. For example if i have" \
                 f"this list '[[apple,  pear, cherry, banana],[ferrari,fiat,mercedes]]' the output is '[fruit,cars]'." \
                 f"If the list has only blank elements answer with none. Names in the list can be repeated." \
                 f"No prose, output format: json list. {json_data}"
        column_names = chatgpt.get_solution(prompt=prompt)["choices"][0].message.content

        return json.loads(column_names)
