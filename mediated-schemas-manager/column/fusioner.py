
class Fusioner:

    def fuse_columns(self, df):

        pass

    def __find_bi_columns(self ,df):
        '''
            necessary condition of bicolumns: two columns must be complementary
        '''
        indexes1 = [i for i in range(df.shape[1] + 1)]
        indexes2 = [i for i in range(df.shape[1] + 1)]

        couples = [(x, y) for x in indexes1 for y in indexes2]

        for couple in couples:
            ind1 = couple[0]
            ind2 = couple[1]
            are_complementary = self.__are_complementary(df.iloc[:, ind1] ,df.iloc[:, ind2])
            if are_complementary:
                self.__check_if_same_attribute(df.iloc[:, ind1] ,df.iloc[:, ind2])


    def __are_complementary(self ,list1 ,list2):
        filled_fields = [0, 0, 0]
        compl_threshold = 0.1
        if len(list1) != len(list2):
            raise Exception("Two lists don't have same size")

        for i in range(0 ,len(list1)):
            pass  # fixme

        return True


    def __check_if_same_attribute(selfself ,col1 ,col2):

        return True