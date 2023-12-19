import pandas as pd


class DataReader:

    def __init__(self, datasets_folder):
        self.__datasets_folder = datasets_folder

    def read_dataset(self, dataset_name):
        df = pd.read_csv(self.__datasets_folder + dataset_name + "/input.csv", header=None)

        return df

    def read_result(self, dataset_name):
        df = pd.read_csv(self.__datasets_folder + dataset_name + "/output.csv")

        return df
