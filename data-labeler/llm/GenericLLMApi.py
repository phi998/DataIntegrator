from enums.chatgpt.Model import Model


class GenericLLMApi:

    def get_simple_solution(self, task="", prompt=""):
        raise NotImplementedError("Subclasses must implement get_response")

    def get_one_shot_solution(self, examples, input_example, output_example, task, prompt, prompt_input, instructions, observations):
        raise NotImplementedError("Subclasses must implement get_response")
