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

    def test_college_recruiter(self):
        self.__run_chain(dataset_name="college_recruiter")

    def test_finanzacom(self):
        self.__run_chain(dataset_name="finanzacom")

    def test_glassdoor(self):
        self.__run_chain(dataset_name="glassdoor")

    def test_indeed(self):
        self.__run_chain(dataset_name="indeed")

    def test_monster(self):
        self.__run_chain(dataset_name="monster")

    def test_wsi(self):
        self.__run_chain(dataset_name="wsi")

    def test_yahoo_finance(self):
        self.__run_chain(dataset_name="yahoo_finance")

    def __run_chain(self, dataset_name):
        dc = DefaultChain()
        df = pd.read_csv('datasets/full/' + dataset_name + '.csv', header=None, na_values='')

        df = dc.apply(df)

        df.to_csv('datasets/full/output/' + dataset_name + '.csv', index=False)


