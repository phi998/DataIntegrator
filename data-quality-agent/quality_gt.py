import csv
import os

import nltk
import pandas as pd
from nltk import word_tokenize

from filesystem import DataReader


def remove_common_prefix_suffix(df):
    result_df = pd.DataFrame()

    for column in df.columns:
        values = df[column].apply(lambda x: str(x) if pd.notna(x) else '').tolist()

        common_prefix = os.path.commonprefix(values)
        common_suffix = os.path.commonprefix([value[::-1] for value in values])[::-1]

        result_df[column] = [value[len(common_prefix):-len(common_suffix)] for value in values]

    return result_df

def write_assocs_results(dataset_name, assocs):
    result_csv_path = f"results/{dataset_name}.csv"
    with open(result_csv_path, 'w', newline='') as csvfile:
        fieldnames = assocs[0].keys()
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(assocs)


def append_assocs_accuracy(dataset_name, assocs):
    total = len(assocs)
    equals = 0

    for assoc in assocs:
        output = assoc["output_colname"]
        expected = assoc["expected_colname"]
        if output == expected:
            equals += 1

    accuracy_csv_file = f'results/accuracy.csv'
    with open(accuracy_csv_file, 'a', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow([dataset_name, equals/total])


def explode_strings_to_tokens(list_of_strings):
    token_list = [word_tokenize(str(string)) for string in list_of_strings]
    return [token for sublist in token_list for token in sublist]


def distance(list1, list2):  # use jaccard distance
    set1 = set(explode_strings_to_tokens(list1))
    set2 = set(explode_strings_to_tokens(list2))

    intersection_size = len(set1.intersection(set2))
    union_size = len(set1.union(set2))

    jaccard_distance = 1.0 - (intersection_size / union_size) if union_size != 0 else 0.0

    return jaccard_distance


def compute_dataset_assocs(gt_df, output_df):
    output_dict = output_df.to_dict(orient='list')
    gt_dict = gt_df.to_dict(orient='list')

    assocs = []

    for k, v in output_dict.items():
        col_name = k
        col_content = v

        dist_sentinel = 1.0
        min_col = ""
        for gtk, gtv in gt_dict.items():
            gt_col_name = gtk
            gt_col_content = gtv
            dist = distance(col_content, gt_col_content)
            if dist <= dist_sentinel:
                dist_sentinel = dist
                min_col = gt_col_name

        assocs.append({"output_colname": col_name, "expected_colname": min_col, "distance": dist_sentinel})

    return assocs


def main():
    nltk.download('punkt')
    dr = DataReader.DataReader("datasets/")

    for dataset_name in os.listdir("datasets/ground_truth"):
        base_name = os.path.splitext(os.path.basename(dataset_name))[0]
        gt_df = dr.read_groundtruth(base_name)
        out_df = dr.read_output(base_name + "_1", 3)

        assocs = compute_dataset_assocs(gt_df, out_df)
        write_assocs_results(base_name, assocs)
        append_assocs_accuracy(base_name, assocs)


if __name__ == "__main__":
    main()
