import json

from confluent_kafka import Producer


class Publisher:

    def __init__(self, server='kafka', port=9092):
        bootstrap_servers = server + ":" + str(port)
        conf = {
            'bootstrap.servers': bootstrap_servers,
        }
        self.producer = Producer(conf)

    def publish_aligned_data(self, message):
        #  topic = "aligned-data-event-channel"
        topic = "job-end-service-event-channel"  # FIXME

        print(f"PUBLISHING ALIGNED DATA: {message}")
        self.producer.produce(topic, key='ad', value=message, callback=self.__delivery_report)
        print("PUBLISHED ALIGNED DATA")

    def publish_inform_manager(self, job_id):
        topic = "manager-informer-event-channel"
        message = {
            "event": "DATA_ALIGNED",
            "data": {"jobId": job_id}
        }

        print(f"INFORMING DATA INTEGRATION MANAGER: {message}")
        self.producer.produce(topic, key='ad', value=json.dumps(message), callback=self.__delivery_report)  # FIXME
        print("INFORMED DATA INTEGRATION MANAGER")

    def __delivery_report(self, err, msg):
        if err is not None:
            print(f'Message delivery failed: {err}')
        else:
            print(f'Message delivered to {msg.topic()} [{msg.partition()}]')
