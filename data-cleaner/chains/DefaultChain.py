from filters.EmptyColumnsCleaner import EmptyColumnsCleaner
from filters.HtmlCleaner import HtmlCleaner
from filters.Labeler import Labeler
from filters.UsefulDataFilter import UsefulDataFilter


class DefaultChain:

    def __init__(self, context, ontology):
        self.context = context
        self.ontology = ontology

    def apply(self, df):
        df = df.iloc[:, 3:]  # delete some columns of naruto output

        html_cleaner = HtmlCleaner()
        df = html_cleaner.clean(df)

        print("Cleaned html data")

        empty_columns_cleaner = EmptyColumnsCleaner(0.4)
        df = empty_columns_cleaner.clean(df)

        print("Cleaned empty columns")

        useful_data_filter = UsefulDataFilter(data_context=self.context)
        df = useful_data_filter.clean(df)

        print("Filtered useful data")

        # TODO manage failures
        labeler = Labeler(data_context=self.context)
        df = labeler.label_columns(df, self.ontology)

        print("Column naming finished")

        return df
