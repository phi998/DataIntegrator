from filters.ColumnEntropyFilter import ColumnEntropyFilter
from filters.ColumnLabeler import ColumnLabeler
from filters.ColumnTrimFilter import ColumnTrimFilter
from filters.EmptyColumnsCleaner import EmptyColumnsCleaner
from filters.HtmlCleaner import HtmlCleaner
from filters.Labeler import Labeler
from filters.UsefulDataFilter import UsefulDataFilter


class DefaultChain:

    def __init__(self, context, ontology):
        self.context = context
        self.ontology = ontology

    def apply(self, df, columns_to_drop=None):
        if columns_to_drop is None:
            columns_to_drop = []

        df = df.drop(index=columns_to_drop)  # delete some columns of naruto output

        html_cleaner = HtmlCleaner()
        df = html_cleaner.clean(df)

        print("Cleaned html data")

        empty_columns_cleaner = EmptyColumnsCleaner(0.4)
        df = empty_columns_cleaner.clean(df)

        print("Cleaned empty columns")

        table_column_trimmer = ColumnTrimFilter()
        df = table_column_trimmer.apply(df)

        '''
        entropy_filter = ColumnEntropyFilter(0.2)
        df = entropy_filter.apply(df, self.ontology)

        print("Cleaned low entropy columns")
        '''

        labeler = ColumnLabeler(data_context=self.context)
        df = labeler.label_columns(df, self.ontology)

        print("Column naming finished")

        return df
