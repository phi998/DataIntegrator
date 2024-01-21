import os

import pandas as pd


class DataReader:

    def __init__(self, datasets_folder):
        self.__datasets_folder = datasets_folder

    def read_dataset(self, dataset_name):
        df = pd.read_csv(os.path.join(self.__datasets_folder, "input", f'{dataset_name}.csv'), header=None)

        return df

    def read_expected(self, dataset_name, case):
        df = pd.read_csv(os.path.join(self.__datasets_folder, "expected", f'case{case}', f'{dataset_name}.csv'), header=None)

        return df

    def read_output(self, dataset_name, case):
        df = pd.read_csv(os.path.join(self.__datasets_folder, "output", f'case{case}', f'{dataset_name}.csv'), header=None)

        return df
