import json

from filesystem import DataReader
from proxy import PipelineClient

dr = DataReader.DataReader("datasets/")
pc = PipelineClient.PipelineClient(base_url="http://localhost:8080")

ontologies_file_path = "ontologies.json"
with open(ontologies_file_path, 'r') as file:
    ontologies = json.load(file)

experiment_name = "companies_2"
df_companies = dr.read_dataset(experiment_name)
companies_ontology = ontologies["companies"]

new_job = pc.create_new_job(job_name=experiment_name, ontology=companies_ontology)
new_job_id = new_job["jobId"]

pc.upload_table(table_name=experiment_name,df=df_companies,job_id=new_job_id,columns_to_ignore=[])

