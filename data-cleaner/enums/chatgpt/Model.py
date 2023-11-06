from enum import Enum


class Model(Enum):
    GPT_3_5 = 'gpt-3.5-turbo'
    GPT_3_5_16K = 'gpt-3.5-turbo-16k'
    GPT_4 = 'gpt-4'

    DEFAULT = 'gpt-3.5-turbo'
