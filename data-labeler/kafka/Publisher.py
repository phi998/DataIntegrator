import json

from confluent_kafka import Producer


class Publisher:

    def __init__(self, server='kafka', port=9092):
        bootstrap_servers = server + ":" + str(port)
        conf = {
            'bootstrap.servers': bootstrap_servers,
        }
        self.producer = Producer(conf)

    def publish_labeled_data(self, job_id, job_name, ontology, tables):
        topic = "labeled-data-event-channel"
        data_cleaned_event = {"jobId": job_id, "jobName": job_name, "ontology": ontology, "data": tables}

        print(f"PUBLISHING LABELED DATA: {data_cleaned_event}")
        self.producer.produce(topic, key='ld', value=json.dumps(data_cleaned_event), callback=self.__delivery_report)
        print("PUBLISHED LABELED DATA")

    def publish_inform_manager(self, job_id):
        topic = "manager-informer-event-channel"

        message = {
            "event": "DATA_LABELED",
            "data": {"jobId": job_id}
        }

        print(f"INFORMING DATA INTEGRATION MANAGER: {message}")
        self.producer.produce(topic, key='ld', value=json.dumps(message), callback=self.__delivery_report)  # FIXME
        print("INFORMED DATA INTEGRATION MANAGER")

    def publish_job_ended_event(self, job_id, job_name, tables):
        topic = "job-end-service-event-channel"

        job_ended_event = {"jobId": job_id, "jobName": job_name, "jobResultResourceUrls": tables}

        print(f"PUBLISHING ENDED JOB: {job_ended_event}")
        self.producer.produce(topic, key='ld', value=json.dumps(job_ended_event), callback=self.__delivery_report)
        print("PUBLISHED ENDED JOB")

    def __delivery_report(self, err, msg):
        if err is not None:
            print(f'Message delivery failed: {err}')
        else:
            print(f'Message delivered to {msg.topic()} [{msg.partition()}]')
