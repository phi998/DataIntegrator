from filters.labeler.LabelerByColumn import LabelerByColumn


class DefaultChain:

    def __init__(self, context, ontology, ontology_name):
        self.__context = context
        self.__ontology = ontology
        self.__ontology_name = ontology_name

    def apply(self, df):

        # Preprocessors
        ## code for preprocessors here

        # Chain core

        print("labeling columns")

        labeler = LabelerByColumn(self.__ontology_name)
        df = labeler.label_columns(df, self.__ontology)

        # Postprocessors
        ## code for postprocessors here

        return df



