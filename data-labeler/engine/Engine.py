from engine.JobWorker import JobWorker
from kafka.Publisher import Publisher
from proxy.FileStorageApi import FileStorageApi
from stats.FilePrinter import FilePrinter


class Engine:

    def __init__(self):
        self.modality = "DEBUG"
        self.dis_endpoint = "http://dis:8080"
        self.publisher = Publisher(server='kafka', port=9092)

    def apply_job(self, job):
        job_worker = JobWorker()

        cleaned_tables = job_worker.clean_tables(job)

        cleaned_tables_urls = []

        file_storage_api = FileStorageApi()

        for cleaned_table in cleaned_tables:
            table_name = cleaned_table["tableName"]
            table_df = cleaned_table["df"]

            table_content_csv = table_df.to_csv(index=False)
            uploaded_file_response = file_storage_api.upload_file(table_name, table_content_csv)
            resource_url = uploaded_file_response["resourceUrl"]

            cleaned_tables_urls.append({"tableName": table_name, "tableUrl": resource_url})

            if self.modality == "DEBUG":
                fp = FilePrinter()
                fp.print_table_to_csv(table_name=table_name, table_content=table_content_csv)

        # Publish to message queues
        self.publisher.publish_cleaned_data(job_id=job["jobId"], job_name=job["jobName"], has_ontology=bool(job["data"]["ontology"]), tables=cleaned_tables_urls)
        self.publisher.publish_inform_manager(job_id=job["jobId"])
        self.publisher.publish_job_ended_event(job_id=job["jobId"], job_name=job["jobName"],tables=cleaned_tables_urls)

