class SchemaCompactor:

    def apply(self, dfs):
        grouped_dfs = []

        for df in dfs:
            df.columns = df.columns.str.replace(r'\.\d+$', '', regex=True)
            grouped_df = self.__compact(df)
            grouped_dfs.append(grouped_df)

        return grouped_dfs

    def __same_merge(self, x):
        return ','.join(x[x.notnull()].astype(str))

    def __compact(self, df):
        df_new = df.groupby(level=0, axis=1).apply(lambda x: x.apply(self.__same_merge, axis=1))

        return df_new
