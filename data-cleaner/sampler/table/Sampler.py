import tiktoken


class Sampler:

    def _count_non_empty_columns_in_new_column(self, df):
        df_clone = df.map(lambda x: None if x == '' else x)
        row_counts = df_clone.count(axis=1)
        df_clone['non_empty_count'] = row_counts

        return df_clone

    def _count_num_of_tokens_in_new_column(self, df):
        df_clone = df
        df_clone['total_token_count'] = 0
        for column in df_clone.columns:
            for index, row in df_clone.iterrows():
                text = row[column]
                token_count = self._num_tokens_from_string(text)
                df_clone.at[index, 'total_token_count'] += token_count

        return df_clone

    def _count_tokens(self, text):
        tokens = str(text).split()
        return len(tokens)

    def _num_tokens_from_string(self, text) -> int:
        encoding = tiktoken.get_encoding("cl100k_base")
        num_tokens = len(encoding.encode(str(text)))
        return num_tokens

    def _count_non_empty_columns(self, row):
        return row.count()
