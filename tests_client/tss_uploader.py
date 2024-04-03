import json
import os

import pandas as pd

from client.DimClient import DimClient
from client.TssClient import TssClient
from filesystem.DataReader import DataReader
from job.JobsManager import JobsManager
from ontology.OntologyManager import OntologyManager


def main(ontology_group, ontology_format, jobs_group):

    dimclient = DimClient("http://localhost:8080")

    om = OntologyManager()
    ontologies = om.get_ontologies(ontology_group, ontology_format)

    for ontology_name, ontology_items in ontologies.items():
        dimclient.create_ontology(ontology_name, ontology_items)

    # read datasets and upload to tss
    dr = DataReader("output_datasets")
    tss_client = TssClient()

    with open(f"jobs/{jobs_group}.json", 'r') as json_file:
        jobs = json.load(json_file)

    for job in jobs["jobs"]:
        job_name = job["name"]
        dataset_name = job["datasetName"]
        ontology_name = job["ontologyName"]

        tss_client.upload_table(ontology_name, job_name, dr.read_dataset(jobs["datasetsGroup"], dataset_name + "_1", 0))


if __name__ == "__main__":
    main(ontology_group="all", ontology_format="json", jobs_group="jobs_all")
