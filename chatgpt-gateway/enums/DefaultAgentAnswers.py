from enum import Enum


class DefaultAgentAnswers(Enum):
    INSTRUCTIONS_PROVIDED = "Ok, I will help you in performing your task based on the instructions you provided"
    EXAMPLE_PROVIDED = "Ok, I'll consider the format you provided in the example"
