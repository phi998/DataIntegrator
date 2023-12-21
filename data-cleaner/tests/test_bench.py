'''
This test is used for adding noise to downloaded datasets
The noise has the purpose to simulate the behaviour of the output_old of Naruto, full of html tags, css, so that it can be added to the original dataset
'''
import json
import string
import unittest
import random

import numpy as np
import pandas as pd

from chains.DefaultChain import DefaultChain


class BenchMarkTest(unittest.TestCase):

    def test_imdb_benchmark(self):
        # tconst	titleType	primaryTitle	originalTitle	isAdult	startYear	endYear	runtimeMinutes	genres
        self.__run_exp(exp_name="imdb", context="imdb", ext='tsv', sep='\t')
        pass

    def __run_exp(self, exp_name, context, ext='csv', sep=','):
        # read dataset
        print("Loading dataset...")
        df = pd.read_csv(f"datasets/bench/input/{exp_name}.{ext}", sep=sep, nrows=1000, header=None, na_values='')
        print("Dataset loaded")

        # add noise to dataset
        print("Adding noise...")
        df = self.__add_noise(df)
        print("Noise added")

        # put it into pipeline
        ontology = self.__read_ontology(domain=context)
        dc = DefaultChain(context=context, ontology=ontology)
        print("Applying default chain...")
        df = dc.apply(df)
        print("Default chain applied")

        # save result to file
        df.to_csv(f"datasets/bench/output/{exp_name}.csv", index=False)

        pass

    def __run_chain(self, dataset_name, context, ontology, cols_to_drop):
        dc = DefaultChain(context=context, ontology=ontology)
        df = pd.read_csv('datasets/full/' + dataset_name + '.csv', header=None, na_values='')

        df = dc.apply(df, cols_to_drop)

        df.to_csv('datasets/full/output/' + dataset_name + '.csv', index=False)

    def __read_ontology(self, domain):
        ontologies_file_path = "ontologies_bench.json"
        with open(ontologies_file_path, 'r') as file:
            ontologies = json.load(file)
        return ontologies[domain]

    def __add_noise(self, df):
        # add html noise
        for colname in df.columns:
            df[colname] = df[colname].apply(self.__add_random_html_string_to_column)

        # add css column
        df = self.__add_random_css_column(df, "css_code")

        # add semi-empty column
        df = self.__add_semiempty_column(df, "semiempty")

        return df

    def __add_random_html_string_to_column(self, text):
        html_random_strings = self.__read_file_line_by_line("html_noise")
        html_random_strings.append("")

        prefix = random.choice(html_random_strings)
        suffix = random.choice(html_random_strings)
        return f"{prefix}{text}{suffix}"

    def __add_random_css_column(self, df,  column_name):
        css_random_strings = self.__read_file_line_by_line("css_noise")
        css_random_strings.append("")

        df[column_name] = np.random.choice(css_random_strings, size=len(df))
        return df

    def __add_semiempty_column(self, df, colname):
        return self.__add_random_strings_column(df, colname, 20, 0.7)

    def __add_random_strings_column(self, df, column_name, string_length, empty_percentage):
        df[column_name] = [self.__generate_random_string(string_length) if random.random() > empty_percentage else None for _
                           in range(len(df))]
        return df

    def __generate_random_string(self, length):
        letters = string.ascii_letters
        return ''.join(random.choice(letters) for _ in range(length))


    def  __read_file_line_by_line(self, filename):
        file_path = f'datasets/bench/{filename}.txt'

        with open(file_path, 'r') as file:
            content_list = file.readlines()

        return content_list
