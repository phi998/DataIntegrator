from functools import lru_cache

from config.ConfigCache import ConfigCache


class CacheFactory:

    @lru_cache(maxsize=1)
    def get_cache(self):
        return ConfigCache()
