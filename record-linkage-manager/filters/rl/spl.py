from splink.duckdb.linker import DuckDBLinker
from splink.duckdb.blocking_rule_library import block_on

# splink

class SPLink:

    def apply(self, df):

        # assigns unique id to each record
        df = df.reset_index()
        df = df.rename(columns={"index": "unique_id"})
        df['unique_id'] = df.index

        linker = DuckDBLinker(df)

        useful_columns_names = self.__get_useful_columns_names(df=df)

        ## delete the new column in df for indexing

        return df

    ##########################
    ## Exploratory Analysis ##
    ##########################

    def __get_useful_columns_names(self, df):

        return []

    ###########################
    ##        Blocking       ##
    ###########################

    def __block_dataset(self, df):


        return df

    def __get_blocking_columns(self, df):
        ## if manual then ..., else ask chatgpt

        return []

