import csv
import json
import os

from engine.QualityMeasurer import QualityMeasurer
from filesystem import DataReader


def main():
    folder_name = "unicredit/"

    qm = QualityMeasurer()
    metrics_dict = {}

    dr = DataReader.DataReader(folder_name)

    for dataset_name in os.listdir(folder_name):

        expected_df = dr.read_expected(dataset_name)
        output_df = dr.read_output(dataset_name)
        #input_df = dr.read_dataset(dataset_name)

        metrics = qm.get_metrics(output_df, expected_df)

        #metrics["input_columns"] = input_df.shape[1]
        metrics["datasetName"] = dataset_name
        metrics["output_columns"] = output_df.shape[1]
        metrics["rows"] = output_df.shape[0]
        metrics["has_ontology"] = False
        metrics["uses_observations"] = True

        metrics_dict[dataset_name] = metrics

    with open('metrics.csv', 'w', newline='') as csvfile:
        # Define the fieldnames based on the keys in the dictionaries
        fieldnames = metrics_dict[list(metrics_dict.keys())[0]].keys()

        # Create a CSV writer
        csv_writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

        # Write the header
        csv_writer.writeheader()

        # Write the data
        csv_writer.writerows(metrics_dict.values())


if __name__ == "__main__":
    main()

# riportare ontologia relativa%