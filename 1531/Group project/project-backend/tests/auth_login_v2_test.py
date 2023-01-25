import pytest
import requests
import json
from src import config
from src.error import InputError

OK = 200 

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_functionality(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "monty@mail.valid", "password": "password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    assert resp.status_code == OK
    
def test_invalid_email(clear):
    info = {"email": "nouser@mail.valid", "password": "password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = info) # log in request
    assert resp.status_code == InputError.code
    
def test_incorrect_password(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "monty@mail.valid", "password": "incorrect"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    assert resp.status_code == InputError.code

