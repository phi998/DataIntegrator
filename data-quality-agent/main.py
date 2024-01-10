import json

from filesystem import DataReader
from proxy import PipelineClient


def main():
    dr = DataReader.DataReader("datasets/")
    pc = PipelineClient.PipelineClient(base_url="http://localhost:8080")

    ontologies_file_path = "ontologies.json"
    with open(ontologies_file_path, 'r') as file:
        ontologies = json.load(file)

    jobs_file_path = "jobs.json"
    with open(jobs_file_path, 'r') as file:
        jobs = json.load(file)

    for job in jobs:
        print(f"Working on job[jobName={job['name']}, datasetName={job['datasetName']}, ontologyName={job['ontologyName']} ]")

        job_df = dr.read_dataset(job['datasetName'])
        job_ontology = ontologies[job['ontologyName']]

        new_job_created_response = pc.create_new_job(job['name'], job_ontology)
        new_job_id = new_job_created_response["jobId"]

        # upload tables
        pc.upload_table(job["name"], job_df, new_job_id)

        # start job
        pc.start_job(new_job_id, job['ignore'])


if __name__ == "__main__":
    main()
