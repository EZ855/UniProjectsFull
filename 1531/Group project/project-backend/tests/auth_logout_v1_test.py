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
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    response_register = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    response_data = response_register.json()
    response_logout = requests.post(config.url + 'auth/logout/v1', json = response_data) # log out request 
    assert response_logout.status_code == OK

def test_invalid_token(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    invalid_token = {'token': 'invalid.token'}
    response_logout = requests.post(config.url + 'auth/logout/v1', json = invalid_token) # log out request with invalid token 
    assert response_logout.status_code == AccessError.code
    
