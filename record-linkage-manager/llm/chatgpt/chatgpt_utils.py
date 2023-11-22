import tiktoken

class ChatGptUtils:

    @staticmethod
    def count_tokens(text):
        encoding = tiktoken.get_encoding("cl100k_base")
        num_tokens = len(encoding.encode(str(text)))
        return num_tokens