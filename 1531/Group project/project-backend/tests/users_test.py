import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError

OK = 200 

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    resp_users = requests.get(config.url + 'users/all/v1', params = {"token": token})
    assert resp_users.status_code == OK
    
def test_invalid_token(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info)
    resp_users = requests.get(config.url + 'users/all/v1', params = {"token": "invalid token"})
    assert resp_users.status_code == AccessError.code
    

