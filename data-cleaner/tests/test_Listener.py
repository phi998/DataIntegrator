import json
from unittest import TestCase

from kafka.Listener import Listener


class TestListener(TestCase):

    def test_job_advertisements(self):
        self.__run_experiment(exp_name="job_adv2")
        pass

    def __run_experiment(self, exp_name):
        json_file_path = f"kafka_messages/{exp_name}.json"
        listener = Listener(server='localhost', port=9092, group_id='data-cleaner')

        with open(json_file_path, 'r', encoding="utf-8") as json_file:
            data = json_file.read()
            listener.manage_received_message(data)
