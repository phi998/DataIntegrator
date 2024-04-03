import os
import re

import pandas as pd


class DataReader:

    def __init__(self, datasets_folder):
        self.__datasets_folder = datasets_folder

    def read_dataset(self, dataset_group, dataset_name, headers=None):
        df = pd.read_csv(os.path.join(self.__datasets_folder, dataset_group, f'{dataset_name}.csv'), header=headers)

        return df
