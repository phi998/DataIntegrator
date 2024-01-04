
class PromptBuilder:

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

    def build_observations_subprompt(self, observations):
        sub_prompt = "\nObservations:"
        i = 0
        for observation in observations:
            sub_prompt += f"\n{i}. {observation}"
            i = i + 1

        return sub_prompt
