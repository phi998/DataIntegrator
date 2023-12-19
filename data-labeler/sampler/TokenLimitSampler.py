import pandas as pd

from sampler.Sampler import Sampler


class TokenLimitSampler(Sampler):

    def __init__(self, token_limit):
        super().__init__()
        self.token_limit = token_limit

    def sample(self, df):
        df_clone = super()._count_non_empty_columns_in_new_column(df)
        df_clone = super()._count_num_of_tokens_in_new_column(df_clone)

        filtered_df = df_clone[df_clone['non_empty_count'] > 0].sort_values(by='non_empty_count', ascending=False)

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
