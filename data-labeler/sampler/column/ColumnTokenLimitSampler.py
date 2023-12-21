import random

from sampler.table.Sampler import Sampler


class ColumnTokenLimitSampler(Sampler):

    def __init__(self, token_limit):
        self.__token_limit = token_limit

    def sample_column(self, df, column_name):
        column_list = list(set(df[column_name].tolist()))  # i want distinct values
        random.shuffle(column_list)
        tokens_count = 0
        column_sample = []

        for cell in column_list:
            cell_tokens = super()._count_tokens(cell)
            tokens_count += cell_tokens
            if tokens_count > self.__token_limit:
                break
            column_sample.append(cell)

        return column_sample
