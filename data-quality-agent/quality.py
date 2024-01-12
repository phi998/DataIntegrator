import json

from engine.QualityMeasurer import QualityMeasurer
from filesystem import DataReader


def main():
    jobs_file_path = "jobs.json"
    with open(jobs_file_path, 'r') as file:
        jobs = json.load(file)

    qm = QualityMeasurer()
    metrics_dict = {}

    dr = DataReader.DataReader("datasets/")

    for job in jobs:
        dataset_name = job["datasetName"]

        expected_df = dr.read_expected(dataset_name)
        output_df = dr.read_result(dataset_name)
        metrics = qm.get_metrics(output_df, expected_df)
        metrics_dict[dataset_name] = metrics

    with open('metrics.json', 'w') as json_file:
        json.dump(metrics_dict, json_file, indent=4)


if __name__ == "__main__":
    main()
