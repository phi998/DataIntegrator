from filters.labeler.LabelerByColumn import LabelerByColumn


class DefaultChain:

    def __init__(self, context, ontology):
        self.__context = context
        self.__ontology = ontology

    def apply(self, df):

        # Preprocessors
        ## code for preprocessors here

        # Chain core

        print("labeling columns")

        labeler = LabelerByColumn(self.__context)
        df = labeler.label_columns(df, self.__ontology)

        # Postprocessors
        ## code for postprocessors here

        return df



