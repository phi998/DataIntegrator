import json
import os.path


class OntologyManager:

    def get_ontologies(self, ontology_group, ontology_format):
        with open(os.path.join("ontologies", ontology_format, f"{ontology_group}.{ontology_format}"), 'r') as json_file:
            ontologies_dict = json.load(json_file)

        return ontologies_dict

    def read_ontology(self, ontology_group, ontology_name, ontology_format):
        ontologies_dict = self.get_ontologies(ontology_group, ontology_format)

        if ontology_name in ontologies_dict:
            return ontologies_dict[ontology_name]

        return None
