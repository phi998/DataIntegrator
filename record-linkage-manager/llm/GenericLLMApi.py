from enums.chatgpt.Model import Model


class GenericLLMApi:

    def get_simple_solution(self, model=Model.DEFAULT, task="", prompt=""):
        raise NotImplementedError("Subclasses must implement get_response")

    def get_one_shot_solution(self, input_example, output_example, task, prompt, prompt_input, instructions,
                              model=Model.DEFAULT):
        raise NotImplementedError("Subclasses must implement get_response")