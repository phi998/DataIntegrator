from filters.SchemaAligner import SchemaAligner
from filters.SchemaCompactor import SchemaCompactor


class DefaultChain:

    def __init__(self, context):
        self.context = context

    def apply(self, dfs, ontology_present):

        print("Compacting tables")

        schema_compactor = SchemaCompactor()
        dfs = schema_compactor.apply(dfs)

        print("Aligning tables...")

        schema_aligner = SchemaAligner()
        df = schema_aligner.align_schemas(dfs=dfs, context=self.context, ontology_present=ontology_present)

        print("Tables have been aligned")

        return df
