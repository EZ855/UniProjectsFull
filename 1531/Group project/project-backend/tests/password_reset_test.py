import pytest
import requests
import json
from src import config
from src.error import InputError
from src.error import AccessError


@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')