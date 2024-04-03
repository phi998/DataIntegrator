
class PromptBuilder:

    def build_prompt_examples(self, examples, input_example, output_example):
        prompt = "\nThis is an example:"

        for example in examples:
            prompt += f"\nInput={example['input']}" \
                      f"\nOutput={example['output']}"

        return prompt

    def build_prompt_example(self, input_example, output_example):
        prompt = "\nThis is an example:"
        prompt += f"\nInput={input_example}" \
                  f"\nOutput={output_example}"

        return prompt

    def build_instructions_subprompt(self, instructions):
        sub_prompt = "\nInstructions:"
        i = 0
        for instruction in instructions:
            sub_prompt += f"\n{i}. {instruction}"
            i = i + 1

        return sub_prompt

    def build_observations_subprompt(self, ontology):
        sub_prompt = "\nObservations:"

        for k, v in ontology.items():
            ontology_item_name = k
            ontology_item_type = v["type"]

            if ontology_item_type == "PROSE":
                sub_prompt += f"\n{ontology_item_name} is a long text"
            elif ontology_item_type == "RATING":
                sub_prompt += f"\n{ontology_item_name} is a value relative to a rating"
            elif ontology_item_type == "DATE":
                sub_prompt += f"\n{ontology_item_name} is a date"
            elif ontology_item_type == "NUMERIC":
                sub_prompt += f"\n{ontology_item_name} is a number"

            if 'notes' in v:
                ontology_item_notes = v['notes']
                if not (ontology_item_notes is None or ontology_item_notes.isspace() or len(ontology_item_notes) == 0):
                    sub_prompt += f"\n{ontology_item_name}: {ontology_item_notes}"

        return sub_prompt
