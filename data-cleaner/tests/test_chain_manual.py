import json
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
        self.__run_chain(dataset_name="almalaurea", context="job advertising")

    def test_bakeca(self):
        self.__run_chain(dataset_name="bakeca", context="job advertising")

    def test_college_recruiter(self):
        self.__run_chain(dataset_name="college_recruiter", context="job advertising")

    def test_finanzacom(self):
        self.__run_chain(dataset_name="finanzacom", context="finance")

    def test_glassdoor(self):
        self.__run_chain(dataset_name="glassdoor",
                         context="job advertising",
                         ontology=self.__read_ontology_list("jobs"),
                         cols_to_drop=[])

    def test_indeed(self):
        self.__run_chain(dataset_name="indeed",
                         context="job advertising",
                         ontology=self.__read_ontology_list("jobs"),
                         cols_to_drop=[])

    def test_monster(self):
        self.__run_chain(dataset_name="monster", context="job advertising")

    def test_wsi(self):
        self.__run_chain(dataset_name="wsi", context="finance")

    def test_imdb(self):
        self.__run_chain(dataset_name="imdb",
                         context="movies",
                         ontology=self.__read_ontology_list("movies"),
                         cols_to_drop=[])

    def test_yahoo_finance(self):
        self.__run_chain(dataset_name="yahoo_finance",
                         context="finance",
                         ontology=self.__read_ontology_list("finance"),
                         cols_to_drop=[])

    def test_ansa_finance(self):
        self.__run_chain(dataset_name="ansa_finance",
                         context="finance",
                         ontology=self.__read_ontology_list("finance"),
                         cols_to_drop=[])

    def test_alibaba(self):
        self.__run_chain(dataset_name="alibaba",
                         context="eshopping",
                         ontology=self.__read_ontology_list("eshopping"),
                         cols_to_drop=[])

    def test_booking(self):
        self.__run_chain(dataset_name="booking",
                         context="hotels",
                         ontology=self.__read_ontology_list("hotels"),
                         cols_to_drop=[])

    def test_tecnocasa(self):
        self.__run_chain(dataset_name="tecnocasa",
                         context="realestate",
                         ontology=self.__read_ontology_list("realestate"),
                         cols_to_drop=[])

    def test_tripadvisor(self):
        self.__run_chain(dataset_name="tripadvisor",
                         context="restaurants",
                         ontology=self.__read_ontology_list("restaurants"),
                         cols_to_drop=[])

    def test_yelp(self):
        self.__run_chain(dataset_name="yelp",
                         context="restaurants",
                         ontology=self.__read_ontology_list("restaurants"),
                         cols_to_drop=[])

    def __run_chain(self, dataset_name, context, ontology, cols_to_drop):
        dc = DefaultChain(context=context, ontology=ontology)
        df = pd.read_csv('datasets/full_manual/' + dataset_name + '.csv', header=None, na_values='')

        df = dc.apply(df, cols_to_drop)

        df.to_csv('datasets/full_manual/output/' + dataset_name + '.csv', index=False)

    def __read_ontology(self, domain):
        ontologies_file_path = "ontologies.json"
        with open(ontologies_file_path, 'r') as file:
            ontologies = json.load(file)
        return ontologies[domain]

    def __read_ontology_list(self, domain):
        ontology = self.__read_ontology(domain)
        return list(ontology.keys())
