import json

from filesystem import DataReader
from proxy import PipelineClient


def main():
    dr = DataReader.DataReader("datasets_1/")
    pc = PipelineClient.PipelineClient(base_url="http://localhost:8080")

    ontologies_file_path = "ontologies.json"
    with open(ontologies_file_path, 'r') as file:
        ontologies = json.load(file)

    jobs_file_path = "jobs/jobs_test.json"
    with open(jobs_file_path, 'r') as file:
        jobs = json.load(file)

    jobs_ids = []

    for job in jobs:
        print(f"Working on job[jobName={job['name']}, datasetName={job['datasetName']}, ontologyName={job['ontologyName']} ]")

        job_df = dr.read_dataset(job['datasetName'])
        job_ontology = ontologies[job['ontologyName']]
        job_ontology = pc.create_ontology(job['ontologyName'], job_ontology)

        new_job_created_response = pc.create_new_job(job['name'], job['ontologyName'])
        new_job_id = new_job_created_response["jobId"]
        jobs_ids.append(new_job_id)

        # upload tables
        pc.upload_table(job["name"], job_df, new_job_id)

        # start job
        pc.start_job(new_job_id, job['ignore'])

    with open('jobs_in_progress.json', 'w') as json_file:
        json.dump(jobs_ids, json_file)


if __name__ == "__main__":
    main()
