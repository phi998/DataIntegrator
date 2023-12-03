import json
import time

from confluent_kafka import Consumer, KafkaError

from engine.Engine import Engine

class Listener:

    def __init__(self, server='kafka', port=9092, group_id='data-cleaner'):
        self.server = server
        self.port = port
        self.group_id = group_id

        self.consumer = None

        self.__create_consumer()

    def listen(self, topic):
        self.consumer.subscribe([topic])

        retry_count = 0
        max_retries = 10

        print("Receiving messages")

        while retry_count < max_retries:
            print(f"Polling attempt {retry_count}")

            msg = self.consumer.poll(2.0)

            if msg is None:
                continue
            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    print(f"Reached end of partition {msg.partition()}, offset: {msg.offset()}")
                elif (
                        msg.error().code() == KafkaError.UNKNOWN_TOPIC_OR_PART
                        or msg.error().code() == KafkaError.LEADER_NOT_AVAILABLE
                ):
                    print(f"Error: {msg.error()}. Retrying...")
                    time.sleep(5)
                    self.__create_consumer()
                    retry_count += 1
                    continue
                else:
                    print(f"Error: {msg.error()}")
            else:
                received_message = msg.value().decode('utf-8')
                print(f"RECEIVED MESSAGE: {received_message}")

                self.manage_received_message(received_message)

    def manage_received_message(self, message):
        job = json.loads(message)

        engine = Engine()
        engine.apply_job(job)

    def close_listener(self):
        self.consumer.close()

    def __create_consumer(self):
        conf = {
            'bootstrap.servers': self.server + ":" + str(self.port),
            'group.id': self.group_id,
            'auto.offset.reset': 'earliest'
        }
        self.consumer = Consumer(conf)

