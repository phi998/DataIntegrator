from confluent_kafka import Producer


class Publisher:

    def __init__(self, server='kafka', port=9092):
        bootstrap_servers = server + ":" + str(port)
        conf = {
            'bootstrap.servers': bootstrap_servers,
        }
        self.producer = Producer(conf)

    def publish_cleaned_data(self, message):
        topic = "cleaned-data-event-channel"
        self.producer.produce(topic, key='cd', value=message, callback=self.__delivery_report)

    def publish_inform_manager(self, job_id):
        topic = "manager-informer-event-channel"
        message = {
            "event": "DATA_CLEANED",
            "data": {"jobId": job_id}
        }

        self.producer.produce(topic, key='cd', value=str(message), callback=self.__delivery_report)  # FIXME

    def __delivery_report(self, err, msg):
        if err is not None:
            print(f'Message delivery failed: {err}')
        else:
            print(f'Message delivered to {msg.topic()} [{msg.partition()}]')
