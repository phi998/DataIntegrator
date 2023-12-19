import json
import unittest
import pandas as pd

from chains.DefaultChain import DefaultChain


class ChainTest(unittest.TestCase):

    def test_default_chain(self):
        dc = DefaultChain()
        df = pd.read_csv('datasets/test.csv', header=None)

        df = dc.apply(df)

        df.to_csv("datasets/full/output_old/test.csv")

    def test_almalaurea(self):
        self.__run_chain(dataset_name="almalaurea", context="job advertising", ontology_key="jobs", cols_to_drop=[0, 1, 2])

    def test_bakeca(self):
        self.__run_chain(dataset_name="bakeca", context="job advertising", ontology_key="jobs", cols_to_drop=[0, 1, 2])

    def test_college_recruiter(self):
        self.__run_chain(dataset_name="college_recruiter", context="job advertising", ontology_key="jobs", cols_to_drop=[0, 1, 2, 3])

    def test_finanzacom(self):
        self.__run_chain(dataset_name="finanzacom", context="finance", ontology_key="finance", cols_to_drop=[0, 1, 2, 3, 4])

    def test_glassdoor(self):
        self.__run_chain(dataset_name="glassdoor", context="job advertising", ontology_key="jobs", cols_to_drop=[0, 1, 2, 3, 4])

    def test_indeed(self):
        self.__run_chain(dataset_name="indeed", context="job advertising", ontology_key="jobs", cols_to_drop=[0, 1, 2])

    def test_monster(self):
        self.__run_chain(dataset_name="monster", context="job advertising", ontology_key="jobs", cols_to_drop=[0, 1, 2, 3])

    def test_wsi(self):
        self.__run_chain(dataset_name="wsi", context="finance", ontology_key="finance", cols_to_drop=[0, 1, 2])

    def test_yahoo_finance(self):
        self.__run_chain(dataset_name="yahoo_finance", context="finance", ontology_key="finance", cols_to_drop=[0, 1, 2, 3])

    def test_ansa_finance(self):
        self.__run_chain(dataset_name="ansa_finance", context="finance", ontology_key="finance", cols_to_drop=[0, 1, 2, 3, 4])

    def test_alibaba(self):
        self.__run_chain(dataset_name="alibaba", context="eshopping", ontology_key="eshopping", cols_to_drop=[0, 1, 2])

    def test_booking(self):
        self.__run_chain(dataset_name="booking", context="hotels", ontology_key="hotels", cols_to_drop=[0, 1, 2])

    def test_tecnocasa(self):
        self.__run_chain(dataset_name="tecnocasa", context="realestate", ontology_key="realestate", cols_to_drop=[0, 1])

    def test_tripadvisor(self):
        self.__run_chain(dataset_name="tripadvisor", context="restaurants", ontology_key="restaurants", cols_to_drop=[0, 1, 2])

    def test_yelp(self):
        self.__run_chain(dataset_name="yelp", context="restaurants", ontology_key="restaurants", cols_to_drop=[0, 1])

    def test_imdb(self):
        self.__run_chain(dataset_name="imdb", context="movies", ontology_key="movies", cols_to_drop=[0, 1, 2, 3, 4])

    def __run_chain(self, dataset_name, context, ontology_key, cols_to_drop):
        dc = DefaultChain(context=context, ontology=self.__read_ontology_list(ontology_key))
        df = pd.read_csv('datasets/full/' + dataset_name + '.csv', header=None, na_values='')

        df = dc.apply(df, cols_to_drop)

        df.to_csv('datasets/full/output/' + dataset_name + '.csv', index=False)

    def __read_ontology(self, domain):
        ontologies_file_path = "ontologies.json"
        with open(ontologies_file_path, 'r') as file:
            ontologies = json.load(file)
        return ontologies[domain]

    def __read_ontology_list(self, domain):
        ontology = self.__read_ontology(domain)
        return list(ontology.keys())
