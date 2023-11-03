import unittest

from llm.chatgpt.chatgpt import ChatGPT


class ChatGptTest(unittest.TestCase):

    def test_get_solution(self):
        chatgpt = ChatGPT()
        solution = chatgpt.get_solution("you are an expert domain about farms", "how do i grow up fruit?")

        print(solution)
