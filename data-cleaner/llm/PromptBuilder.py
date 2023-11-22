
class PromptBuilder:

    def build_prompt_with_instructions(self, prompt, instructions):
        prompt += self.__add_instructions(prompt=prompt, instructions=instructions)

        return prompt

    def build_prompt_example(self, input_example, output_example):
        prompt = "\nThis is an example:"
        prompt += f"\nInput={input_example}" \
                  f"\nOutput={output_example}"

        return prompt

    def __add_instructions(self, prompt, instructions):
        prompt += "\nInstructions:"
        i = 0
        for instruction in instructions:
            prompt += f"\n{i}. {instruction}"
            i = i + 1

        return prompt
