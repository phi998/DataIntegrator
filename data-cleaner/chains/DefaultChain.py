from filters.ColumnEntropyFilter import ColumnEntropyFilter
from filters.LabelerByColumn import LabelerByColumn
from filters.ColumnTrimFilter import ColumnTrimFilter
from filters.EmptyColumnsCleaner import EmptyColumnsCleaner
from filters.HtmlCleaner import HtmlCleaner
from stats.StatisticsCache import StatisticsCache


class DefaultChain:

    def __init__(self, context, ontology):
        self.context = context
        self.ontology = ontology

    def apply(self, df, columns_to_drop=None):
        if columns_to_drop is None:
            columns_to_drop = []

        df = df.drop(df.columns[columns_to_drop], axis=1)

        html_cleaner = HtmlCleaner()
        df = html_cleaner.clean(df)

        print("Cleaned html data")

        empty_columns_cleaner = EmptyColumnsCleaner(0.4)
        df = empty_columns_cleaner.clean(df)

        print("Cleaned empty columns")

        table_column_trimmer = ColumnTrimFilter()
        df = table_column_trimmer.apply(df)

        entropy_filter = ColumnEntropyFilter(1/len(df) + 0.0001)  # FIXME
        df = entropy_filter.apply(df, self.ontology)

        print("Cleaned low entropy columns")

        ### STATISTICS ###
        statistics_cache = StatisticsCache().get_instance()
        statistics_cache.set_number_of_columns(len(df.columns))
        ##################

        labeler = LabelerByColumn(data_context=self.context)
        df = labeler.label_columns(df, self.ontology)

        print("Column naming finished")

        return df
