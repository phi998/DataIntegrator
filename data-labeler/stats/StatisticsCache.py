import csv
from datetime import datetime

from config.ConfigCache import ConfigCache


class StatisticsCache:
    _instance = None

    def __init__(self):
        if not StatisticsCache._instance:
            StatisticsCache._instance = self
            self.__stats = {}
            self.__fails = {}

    @staticmethod
    def get_instance():
        if not StatisticsCache._instance:
            StatisticsCache._instance = StatisticsCache()
        return StatisticsCache._instance

    def init_cache(self, dataset_name, context):
        self.__init_dictionary(dataset_name, context)

    def __init_dictionary(self, dataset_name, context):
        self.__stats["dataset_name"] = dataset_name
        self.__stats["context"] = context

        config_cache = ConfigCache()
        self.__stats["retry_timewait"] = config_cache.get_configuration("llm_fail_timewait_secs")
        self.__stats["ontology_enabled"] = config_cache.get_configuration("ontology_enabled")
        self.__stats["observations_enabled"] = config_cache.get_configuration("observations_enabled")

        self.__stats["llm_retries"] = 0
        self.__stats["fails"] = []

    def set_number_of_columns(self, num_of_columns):
        self.__stats["num_of_columns"] = num_of_columns

    def add_fail(self, prompt_input, motivation):
        self.__stats["llm_retries"] += 1
        self.__stats["fails"].append({
            "prompt_input": prompt_input,
            "motivation": motivation,
            "input_length": len(prompt_input)
        })

    def set_execution_time(self, begin_timestamp, end_timestamp):
        self.__stats["execution_time_secs"] = end_timestamp - begin_timestamp
        self.__stats["begin_timestamp"] = begin_timestamp

    def print_statistics(self, stats_file):
        try:
            with open(stats_file, mode='a', newline='') as csv_file:
                fieldnames = ["Dataset Name", "Context", "Number of Columns", "Execution Time (secs)",
                              "Number of LLM Retries", "Retry Timewait (secs)", "Ontology Enabled",
                              "Observations Enabled", "Begin Timestamp"]
                writer = csv.DictWriter(csv_file, fieldnames=fieldnames)

                writer.writerow({
                    "Dataset Name": self.__stats["dataset_name"],
                    "Context": self.__stats["context"],
                    "Number of Columns": self.__stats.get("num_of_columns", "Not specified"),
                    "Execution Time (secs)": self.__stats.get("execution_time_secs", "Not available"),
                    "Number of LLM Retries": self.__stats.get("llm_retries", 0),
                    "Retry Timewait (secs)": self.__stats.get("retry_timewait", "Not specified"),
                    "Ontology Enabled": self.__stats.get("ontology_enabled", "Not specified"),
                    "Observations Enabled": self.__stats.get("observations_enabled", "Not specified"),
                    "Begin Timestamp": self.__stats.get("begin_timestamp", "Not specified"),
                })

            print("Statistics saved to CSV file: {}".format(stats_file))
        except Exception as e:
            print("Error saving statistics to CSV file: {}".format(e))

    def print_failures(self, failures_file):
        try:
            with open(failures_file, mode='a', newline='') as csv_file:
                fieldnames = ["Date", "Dataset name", "Context", "Prompt input", "Motivation", "Input length"]
                writer = csv.DictWriter(csv_file, fieldnames=fieldnames)

                for failure in self.__stats["fails"]:
                    prompt_input = failure["prompt_input"]
                    motivation = failure["motivation"]
                    input_length = failure["input_length"]

                    writer.writerow({
                        "Dataset name": self.__stats["dataset_name"],
                        "Date": self.__stats["begin_timestamp"],
                        "Context": self.__stats["context"],
                        "Prompt input": prompt_input,
                        "Motivation": motivation,
                        "Input length": input_length,
                    })

            print("Statistics failures to CSV file: {}".format(failures_file))
        except Exception as e:
            print("Error saving failures to CSV file: {}".format(e))

    def print_to_file(self, stats_file, fails_file):
        self.print_statistics(stats_file)
        self.print_failures(fails_file)
