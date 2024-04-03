import json
import os

from client.DimClient import DimClient
from filesystem.DataReader import DataReader
from ontology.OntologyManager import OntologyManager


class JobsManager:

    def __init__(self, dim_base_url, jobs_group_name):
        self.__dim_base_url = dim_base_url

        jobs_info = self.__get_jobs_group_info(jobs_group_name)

        self.__ontology_group_name = jobs_info["ontologyGroup"]
        self.__ontology_format = jobs_info["ontologyFormat"]
        self.__datasets_group = jobs_info["datasetsGroup"]
        self.__jobs = jobs_info["jobs"]

    def __get_jobs_group_info(self, jobs_group_name):
        jobs_file_path = os.path.join("jobs", f"{jobs_group_name}.json")
        with open(jobs_file_path, 'r') as file:
            jobs_info = json.load(file)

        return jobs_info

    def run_jobs(self):
        dr = DataReader("datasets/")

        om = OntologyManager()

        dimclient = DimClient(self.__dim_base_url)

        for job in self.__jobs:

            job_name = job["name"]
            job_dataset_name = job["datasetName"]
            job_ontology_name = job["ontologyName"]
            job_ignore_columns = job["ignore"]

            input_df = dr.read_dataset(self.__datasets_group, job_dataset_name)

            dimclient.create_ontology(job_ontology_name, om.read_ontology(self.__ontology_group_name, job_ontology_name, self.__ontology_format))

            new_job_created_response = dimclient.create_new_job(job_name, job_ontology_name)
            new_job_id = new_job_created_response["jobId"]

            dimclient.upload_table(job["name"], input_df, new_job_id)
            dimclient.start_job(new_job_id, job_ignore_columns)
