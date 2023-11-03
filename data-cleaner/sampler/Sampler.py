import pandas as pd
import tiktoken

class Sampler:

    def __init__(self):
        self.token_limit = 3500

    def sample(self, df):
        df_clone = df.applymap(lambda x: None if x == '' else x) # fai attenzione
        row_counts = df_clone.count(axis=1)
        df['non_empty_count'] = row_counts

        df['total_token_count'] = 0
        for column in df.columns:
            for index, row in df.iterrows():
                text = row[column]
                token_count = self.__num_tokens_from_string(text)
                df.at[index, 'total_token_count'] += token_count

        filtered_df = df[df['non_empty_count'] > 0].sort_values(by='non_empty_count', ascending=False)

        sampled_rows = pd.DataFrame()
        total_tokens = 0

        for index, row in filtered_df.iterrows():
            if total_tokens + row['total_token_count'] <= self.token_limit:
                sampled_rows = pd.concat([sampled_rows, pd.DataFrame([row])], ignore_index=True)
                total_tokens += row['total_token_count']
            else:
                break

        sampled_rows = sampled_rows.iloc[:, :-2]

        return sampled_rows

    def __count_tokens(self, text):
        tokens = str(text).split()
        return len(tokens)

    def __num_tokens_from_string(self, text) -> int:
        encoding = tiktoken.get_encoding("cl100k_base")
        num_tokens = len(encoding.encode(str(text)))
        return num_tokens

    def __count_non_empty_columns(self, row):
        return row.count()