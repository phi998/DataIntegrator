import json

from filesystem import DataReader


def main():
    jobs_file_path = "jobs.json"
    with open(jobs_file_path, 'r') as file:
        jobs = json.load(file)

    dr = DataReader.DataReader("datasets/")

    for job in jobs:
        dataset_name = job["datasetName"]

        cleaned_df = dr.read_cleaned(dataset_name)


    pass


if __name__ == "__main__":
    main()
