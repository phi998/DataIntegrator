import unittest
import pandas as pd
import logging

from chains.DefaultChain import DefaultChain


class ChainTest(unittest.TestCase):

    def test_default_chain(self):
        dc = DefaultChain()
        df = pd.read_csv('datasets/test.csv', header=None)

        df = dc.apply(df)

        df.to_csv("datasets/full/output/test.csv")

    def test_almalaurea(self):
        self.__run_chain(dataset_name="almalaurea")

    def test_bakeca(self):
        self.__run_chain(dataset_name="bakeca")

    def __run_chain(self, dataset_name):
        logging.basicConfig(format='%(levelname)s:%(message)s', level=logging.DEBUG)

        dc = DefaultChain()
        df = pd.read_csv('datasets/full/' + dataset_name + '.csv', header=None, na_values='')

        df = dc.apply(df)

        df.to_csv('datasets/full/output/' + dataset_name + '.csv', index=False)


