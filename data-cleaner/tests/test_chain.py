import json
import os
import unittest
import time

import pandas as pd

from chains.DefaultChain import DefaultChain
from stats.StatisticsCache import StatisticsCache


class ChainTest(unittest.TestCase):

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
        statistics_cache = StatisticsCache().get_instance()
        statistics_cache.init_cache(dataset_name, context)

        dc = DefaultChain(context=context, ontology=self.__read_ontology(ontology_key))
        df = pd.read_csv('datasets/full/' + dataset_name + '.csv', header=None, na_values='')

        start_time = time.time()
        df = dc.apply(df, cols_to_drop)
        end_time = time.time()
        statistics_cache.set_execution_time(start_time, end_time)

        df.to_csv('datasets/full/output/' + dataset_name + '.csv', index=False)

        statistics_cache.print_to_file(stats_file="stats/stats.csv", fails_file="stats/fails.csv")
        self.__print_columns(dataset_name, df)

    def __read_ontology(self, domain):
        ontologies_file_path = "ontologies.json"
        with open(ontologies_file_path, 'r') as file:
            ontologies = json.load(file)
        return ontologies[domain]

    def __print_columns(self, exp_name, df):
        column_names_string = ','.join(map(str, df.columns)) + "\n"
        with open(f"colnames/{exp_name}.csv", 'a') as file:
            file.write(column_names_string)
