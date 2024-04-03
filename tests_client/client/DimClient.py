import base64

import requests


class DimClient:

    def __init__(self, base_url):
        self.__base_url = base_url

    def create_ontology(self, ontology_name, ontology):

        prev_ontology = self.__get_ontology_by_name(ontology_name)
        if prev_ontology is not None:
            return prev_ontology

        url_suffix = "/ontology"

        ontology_list = []
        for k, v in ontology.items():
            item_name = k
            item_type = v["type"]
            item_importance = v["importance"]

            ontology_list.append({
                "label": item_name,
                "type": item_type,
                "importance": item_importance
            })

        response = requests.post(self.__base_url + url_suffix, json={"name": ontology_name, "items": ontology_list})

        return response.json()

    def create_new_job(self, job_name, ontology_name):
        url_suffix = "/jobs"

        data = {
            "jobName": job_name,
            "ontologyName": ontology_name,
            "jobType": "COLUMN_NAMING"
        }

        response = requests.post(self.__base_url + url_suffix, json=data)

        return response.json()

    def upload_table(self, table_name, df, job_id, columns_to_ignore=None):
        url_suffix = f"/jobs/{job_id}/tables"

        csv_data = df.to_csv(index=False)
        base64_encoded = base64.b64encode(csv_data.encode('utf-8')).decode('utf-8')

        data = {
            "tableName": table_name,
            "tableContent": base64_encoded
        }

        requests.post(self.__base_url + url_suffix, json=data)

    def start_job(self, job_id, columns_to_drop=None):
        if columns_to_drop is None:
            columns_to_drop = []

        url_suffix = f"/jobs/{job_id}/start"

        data = {
            "columnsToDrop": columns_to_drop
        }

        requests.post(self.__base_url + url_suffix, json=data)

    def get_job_result(self, job_id):
        url_suffix = f"/jobs/{job_id}?showTables=true"

        job = requests.get(self.__base_url + url_suffix).json()

        job_name = job["name"]
        job_status = job["status"]
        job_tables = job["endedJobTables"]

        job_out = {
            "name": job_name,
            "status": job_status,
            "tables": job_tables
        }

        return job_out

    def __get_ontology_by_name(self, ontology_name):
        url_suffix = "/ontology"

        query_params = {
            "name": ontology_name
        }

        response = requests.get(self.__base_url + url_suffix, params=query_params)

        print(f"Get ontology by name {ontology_name}: {response}")

        if len(response.json()["ontologies"]) == 0:
            return None

        return response.json()["ontologies"][0]

