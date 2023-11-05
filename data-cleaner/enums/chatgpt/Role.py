from enum import Enum


class Role(Enum):
    SYSTEM = "system"
    AGENT = "agent"
    USER = "user"
