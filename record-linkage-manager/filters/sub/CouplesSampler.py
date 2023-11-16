from itertools import combinations

import pandas as pd
from nltk import ngrams


class CouplesSampler:

    def __init__(self):
        self.max_samples = 1000
        self.top_rows = int(0.5 * self.max_samples)
        self.bottom_rows = int(0.3 * self.max_samples)
        self.middle_rows = int((1 - self.top_rows - self.bottom_rows) * self.max_samples)

    def sample_couples(self, dfs, important_attributes):
        big_schema = self.__compute_big_schema(dfs)
        terms_weights = self.__compute_terms_weights(big_schema, important_attributes)

        merged_df = pd.merge(big_schema, big_schema, how='cross', suffixes=('_l', '_r'))
        merged_df = merged_df[(merged_df['table_id_l'] != merged_df['table_id_r'])]

        important_attributes_dupl = self.__transform_list(important_attributes)
        merged_df = merged_df.dropna(subset=important_attributes_dupl)

        merged_df['distance'] = merged_df.apply(self.__compute_jaccard_distance,
                                                args=(important_attributes, terms_weights), axis=1)
        merged_df = merged_df.sort_values(by='distance')

        top_df = merged_df.head(self.top_rows)
        middle_df = merged_df.iloc[int((len(merged_df)/2 - self.middle_rows / 2)):int((len(merged_df)/2 + self.middle_rows / 2))]
        bottom_df = merged_df.iloc[int(len(merged_df)/2):].sample(n=self.bottom_rows)

        result_df = pd.concat([top_df, middle_df, bottom_df])
        result_df = result_df.reset_index(drop=True)

        result_df = result_df.drop(columns=['distance'])  # do i really need to drop it?

        return result_df

    def __compute_big_schema(self, dfs):
        mediated_schema_column_names = dfs[0].columns.tolist()
        mediated_schema_column_names.append("table_id")

        big_schema = pd.DataFrame(columns=mediated_schema_column_names)

        i = 0
        for df in dfs:
            df["table_id"] = i
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
