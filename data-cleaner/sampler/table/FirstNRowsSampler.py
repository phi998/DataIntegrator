from sampler.table.Sampler import Sampler


class FirstNRowsSampler(Sampler):

    def sample(self, df, n_rows):
        df_clone = super()._count_non_empty_columns_in_new_column(df)
        df_clone = super()._count_num_of_tokens_in_new_column(df_clone)

        filtered_df = df_clone[df_clone['non_empty_count'] > 0].sort_values(by='non_empty_count', ascending=False)

        filtered_df = filtered_df.drop(filtered_df.columns[-2:], axis=1)
        return filtered_df.head(n_rows)






