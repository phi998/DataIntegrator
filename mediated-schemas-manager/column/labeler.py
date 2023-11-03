import json
from llm.chatgpt.chatgpt import ChatGPT


class Labeler:

    def label_columns(self, df):
        df.columns = self.__get_column_names(df)
        return df

    def __get_column_names(self, df):
        csv_data = df.to_csv()
        num_columns = df.shape[1]

        chatgpt = ChatGPT()
        prompt_template = f"Sto per fornirti una stringa che rappresenta una tabella csv con {num_columns} colonne anonime. " \
                          f"Devi fornire in output il nome delle colonne più appropriato sotto forma di una lista json " \
                          f"della forma [x,y,z,...]. Devi fornire il nome in inglese." \
                          f"I nomi delle colonne non devono essere della forma Column1, ..." \
                          f"\n {csv_data}"
        column_names = chatgpt.get_solution(prompt=prompt_template)["choices"][0].message.content

        return json.loads(column_names)
