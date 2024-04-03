


class ColumnEntropyFilter:

    def __init__(self, min_entropy):
        self.__min_entropy = min_entropy

    def apply(self, df):

        for col in df.columns:
            cells_list = df[col].tolist()
            entropy = self.__compute_column_entropy(cells_list)
            if entropy < self.__min_entropy:
                df = df.drop(col, axis=1)

        return df

    def __compute_column_entropy(self, cells_list):
        list_length = len(cells_list)
        distinct_count = len(set(cells_list))

        return distinct_count / list_length
