

class UnlabeledColumnsDropper:

    def __init__(self):
        self.__admit_out_of_ontology = True  # get from config file
        self.vocab_del = ['unknown', 'other']

    def apply(self, df, ontology):

        pass
