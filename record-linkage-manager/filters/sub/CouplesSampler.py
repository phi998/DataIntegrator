import random
from itertools import combinations

import pandas as pd


class CouplesSampler:

    def __init__(self, n_samples):
        self.n_samples = n_samples

    def sample_couples(self, dfs, important_attributes, dfs_to_sample=0):

        if dfs_to_sample > 0:
            dfs = random.sample(dfs, dfs_to_sample)

        num_of_dfs = len(dfs)
        combs = list(combinations(range(0, num_of_dfs), 2))

        final_df = pd.DataFrame()

        # TODO Introduce concurrency
        # TODO optimize only sampling a subset of dfs in input, i need only a sampling!

        for combination in combs:
            left_table_index = combination[0]
            right_table_index = combination[1]

            df1 = dfs[left_table_index]
            df2 = dfs[right_table_index]
            partial_df = self.__sample_couple_sub(df1, df2, important_attributes)
            partial_df["table_id_l"] = left_table_index
            partial_df["table_id_r"] = right_table_index
            final_df = pd.concat([final_df, partial_df], ignore_index=True)
            final_df.reset_index(drop=True, inplace=True)

        final_df = self.__delete_duplicates(df=final_df, important_attributes=important_attributes)

        # sample better
        # final_df = final_df.sample(n=self.n_samples)

        return final_df

    def __sample_couple_sub(self, df1, df2, important_attributes):
        big_schema = self.__compute_big_schema_couple(df1, df2)
        result_df = pd.DataFrame()

        missing_columns = [col for col in important_attributes if col not in big_schema.columns]

        if not missing_columns:
            terms_weights = self.__compute_terms_weights(big_schema, important_attributes)

            merged_df = pd.merge(big_schema, big_schema, how='cross', suffixes=('_l', '_r'))

            # TODO delete couple where right=left, exact matches are useless
            for important_attribute in important_attributes:
                important_attribute_l = important_attribute + "_l"
                important_attribute_r = important_attribute + "_r"

                merged_df = merged_df[merged_df[important_attribute_l] != merged_df[important_attribute_r]]

            important_attributes_dupl = self.__transform_list(important_attributes)
            merged_df = merged_df.dropna(subset=important_attributes_dupl)

            if len(merged_df) > 0:
                merged_df['distance'] = merged_df.apply(self.__compute_jaccard_distance,
                                                        args=(important_attributes, terms_weights), axis=1)
                merged_df = merged_df.sort_values(by='distance')
                result_df = merged_df

        return result_df

    def __compute_big_schema_couple(self, df1, df2):
        return self.__compute_big_schema([df1, df2])

    def __compute_big_schema(self, dfs):
        mediated_schema_column_names = dfs[0].columns.tolist()

        big_schema = pd.DataFrame(columns=mediated_schema_column_names)

        i = 0
        for df in dfs:
            df = df.loc[:, ~df.columns.duplicated()]
            df.reset_index(drop=True, inplace=True)
            big_schema = pd.concat([big_schema, df], join='outer', ignore_index=True)
            big_schema.reset_index(inplace=True, drop=True)
            i += 1

        return big_schema

    def __compute_jaccard_distance(self, row, important_attributes, terms_weights):
        jaccard_sum = 0
        divisor = len(important_attributes)

        for attribute in important_attributes:
            str1 = row[attribute + "_l"]
            str2 = row[attribute + "_r"]
            jaccard_sum += self.__jaccard_distance(str1, str2, terms_weights[attribute])

        return jaccard_sum / divisor

    def __jaccard_distance(self, str1, str2, terms_weight):
        tokens1 = set(str(str1).lower().split())
        tokens2 = set(str(str2).lower().split())

        intersection_weight = 0
        union_weight = 0

        for token in tokens1.intersection(tokens2):
            intersection_weight += terms_weight[token]

        for token in tokens1.union(tokens2):
            union_weight += terms_weight[token]

        jaccard_distance = 1 - (intersection_weight / union_weight)
        return jaccard_distance

    def __transform_list(self, input_list):
        transformed_list = [f'{item}_l' for item in input_list] + [f'{item}_r' for item in input_list]
        return transformed_list

    def __compute_terms_df(self, big_schema, important_attributes):
        term_count_df = {}

        for attribute in important_attributes:
            term_count_df[attribute] = {}
            for cell in big_schema[attribute]:
                tokens = set(str(cell).split())
                for token in tokens:
                    if token in term_count_df[attribute]:
                        term_count_df[attribute][token] += 1
                    else:
                        term_count_df[attribute][token] = 1

        return term_count_df

    def __compute_terms_weights(self, big_schema, important_attributes):
        term_count_df = self.__compute_terms_df(big_schema, important_attributes)
        num_of_documents = len(big_schema)

        term_weights = {}

        for k, v in term_count_df.items():
            attribute = k
            terms_df = v

            term_weights[attribute] = {}

            for term, df in terms_df.items():
                term_weights[attribute][term.lower()] = 1 - (df / num_of_documents)

        return term_weights

    def __delete_duplicates(self, df, important_attributes):
        attrs_to_check = self.__transform_list(important_attributes)
        df = df.drop_duplicates(subset=attrs_to_check, keep='first')

        return df