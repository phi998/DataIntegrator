from config.ConfigCache import ConfigCache
from enums.chatgpt.Model import Model
from llm.chatgpt.chatgpt import ChatGPT
from sampler.column.ColumnTokenLimitSampler import ColumnTokenLimitSampler


class LabelerByColumn:

    def __init__(self, data_context):
        self.__data_context = data_context
        self.__cache = ConfigCache().get_instance()

    def label_columns(self, df, ontology=None):
        if ontology is None:
            ontology = {}

        sampler = ColumnTokenLimitSampler(self.__cache.get_configuration("column_token_limit_for_llm"))

        print("Labelling columns...")

        columns_labels_dict = {}
        for i in df.columns:
            print(f"Labeling column {i}")
            column_values = sampler.sample_column(df, i)
            try:
                column_name = self.__label_column(column_values, ontology)
            except Exception as e:
                print(f"Could not label column {i}, motivation: {e}")
                column_name = "Unknown"
            columns_labels_dict[int(i)] = column_name

        print(f"Got columns_labels_dict={columns_labels_dict}")

        columns_to_keep = list(columns_labels_dict.keys())
        columns_names = list(columns_labels_dict.values())

        df = df[[int(x) for x in columns_to_keep]]
        df.columns = columns_names

        return df

    def __label_column(self, column, ontology):
        chatgpt = ChatGPT(model=Model.GPT_3_5_16K)

        column = [str(col) for col in column if col is not None]

        ontology_items = ','.join(ontology.keys())
        ontology_subprompt = ""

        if self.__cache.get_configuration("ontology_enabled"):
            ontology_subprompt = "Classify the column given to you into only one of these types that are separated with " \
                                 f"comma: {ontology_items}"

        response = chatgpt.get_one_shot_solution(
            input_example='apple, banana, pear, pineapple',
            output_example='{"column_name":"fruit","confidence": 0.9}',
            task=f"You are an expert about {self.__data_context}",
            prompt="Answer the question based on the task below, If the question cannot be answered "
                   "using the information provided answer with 'other'.\n" + ontology_subprompt,
            instructions=["Look at items values in detail",
                          "Select a class that best represents the meaning of all items",
                          "For the selected class write the confidence you would give in the interval between 0 and 1",
                          "Answer with only a json string like {\"column_name\":\"effective name\",\"confidence\":confidence}, no prose"],
            prompt_input=';'.join(column),
            observations=self.__build_observations_list(ontology)
        )

        return response["column_name"]

    def __build_observations_list(self, ontology):
        observations = []

        for k, v in ontology.items():
            ontology_item_name = k
            ontology_item_type = v["type"]

            if ontology_item_type == "PROSE":
                observations.append(f"{ontology_item_name} is a long text")
            elif ontology_item_type == "RATING":
                observations.append(f"\n{ontology_item_name} is a value relative to a rating")
            elif ontology_item_type == "DATE":
                observations.append(f"\n{ontology_item_name} is a date")
            elif ontology_item_type == "NUMERIC":
                observations.append(f"\n{ontology_item_name} is a number")

            if 'notes' in v and not v["notes"] == "":
                ontology_item_notes = v["notes"]
                observations.append(f"\n{ontology_item_name}: {ontology_item_notes}")

        return observations
