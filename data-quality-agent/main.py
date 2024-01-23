import json
import os
import shutil
import time

from filesystem import DataReader
from proxy import PipelineClient


def get_ontology(job_dataset_name, ontology_name):
    with open('ontologies.json', 'r') as json_file:
        ontologies_list = json.load(json_file)

    if ontology_name in ontologies_list:
        return ontologies_list[ontology_name]

    # generate default ontology from ground truth
    dr = DataReader.DataReader("datasets/")
    df = dr.read_groundtruth(job_dataset_name)
    column_names = df.columns.tolist()
    ontology = {}
    for column_name in column_names:
        ontology[column_name] = {
            "type": "TITLE",
            "importance": 5
        }
    return ontology


def get_datasets_list(folder_name):
    return os.listdir(folder_name)


def start_job(job, case):
    dr = DataReader.DataReader("datasets/")
    pc = PipelineClient.PipelineClient(base_url="http://localhost:8080")

    job_name = job["name"]
    job_dataset_name = job["datasetName"]
    job_ontology_name = job["ontologyName"]
    job_ignore_columns = job["ignore"]

    job_ontology = get_ontology(job_dataset_name, job_ontology_name)

    input_df = dr.read_dataset(job_dataset_name)

    pc.create_ontology(job_ontology_name, job_ontology)
    new_job_created_response = pc.create_new_job(job_name, job_ontology_name)
    new_job_id = new_job_created_response["jobId"]

    pc.upload_table(job["name"], input_df, new_job_id)
    pc.start_job(new_job_id, job_ignore_columns)

    return {"jobId": new_job_id}


def main(jobs_filename, case):
    jobs_file_path = jobs_filename
    with open(jobs_file_path, 'r') as file:
        jobs = json.load(file)

    jobs_ids = []

    for job in jobs:
        job_response = start_job(job, case)
        jobs_ids.append(job_response["jobId"])

        time.sleep(1)

    with open('jobs_in_progress.json', 'w') as json_file:
        json.dump(jobs_ids, json_file)


if __name__ == "__main__":
    main("jobs/jobs_groundtruth.json", 1)
