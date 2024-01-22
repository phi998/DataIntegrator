import csv
import os

from engine.QualityMeasurer import QualityMeasurer
from filesystem import DataReader

cases = {
    1: {
        "ontology": False,
        "shots": 1
    },
    2: {
        "ontology": False,
        "shots": 5
    },
    3: {
        "ontology": True,
        "shots": 1
    },
    4: {
        "ontology": True,
        "shots": 5
    }
}


def main():
    folder_name = "datasets/"

    try:
        os.remove("metrics.csv")
    except FileNotFoundError:
        print(f"File 'metrics.csv' not found.")
    except Exception as e:
        print(f"An error occurred: {e}")

    qm = QualityMeasurer()
    metrics_dict = {}

    dr = DataReader.DataReader(folder_name)

    for i in range(1, 5, 1):
        case = cases[i]
        ontology_enabled = case["ontology"]
        shots = case["shots"]

        for dataset_name in os.listdir(os.path.join(folder_name, "input")):
            dataset_name = os.path.splitext(os.path.basename(dataset_name))[0] + "_1"

            expected_df = dr.read_expected(dataset_name, i)
            output_df = dr.read_output(dataset_name, i)

            metrics = qm.get_metrics(output_df, expected_df)

            numeric_columns_len = len(output_df.select_dtypes(include=['number']).columns)

            metrics["datasetName"] = dataset_name
            metrics["output_columns"] = output_df.shape[1]
            metrics["numeric_columns"] = numeric_columns_len
            metrics["rows"] = output_df.shape[0]
            metrics["has_ontology"] = ontology_enabled
            metrics["shots"] = shots

            metrics_dict[dataset_name] = metrics

        fieldnames = metrics_dict[list(metrics_dict.keys())[0]].keys()
        if not os.path.exists("metrics.csv"):
            with open('metrics.csv', 'w', newline='') as csvfile:
                csv_writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
                csv_writer.writeheader()

        with open('metrics.csv', 'a', newline='') as csvfile:
            csv_writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            csv_writer.writerows(metrics_dict.values())


if __name__ == "__main__":
    main()
