from unittest import TestCase

from engine.ChatGPT import ChatGPT
from enums.Model import Model


class TestChatGPT(TestCase):

    def test_interact(self):
        chatgpt = ChatGPT()
        chatgpt.interact("You are an expert about insects", "What's the color of a fly")
