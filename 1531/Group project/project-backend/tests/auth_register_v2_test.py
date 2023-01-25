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
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == OK
    
def test_handle_str_limit(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'montymontymontymonty!', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == OK


def test_auth_register_empty_email(clear):
    info = {'email':'' , 'password': 'password' , 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code

def test_auth_register_invalid_email(clear):
    info = {'email': 'monty@someplace' , 'password': 'password' , 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code
    
def test_auth_register_invalid_password(clear):
    info = {'email':'monty@mail.valid' , 'password': 'four' , 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code

def test_auth_register_empty_name_first(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password' , 'name_first': '', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code
    
def test_auth_register_invalid_name_first(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password' , 'name_first': 'a' * 51, 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code
    
def test_auth_register_empty_name_last(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password' , 'name_first': 'monty', 'name_last': ''}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code

def test_auth_register_invalid_name_last(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password' , 'name_first': 'monty', 'name_last': 'b' * 51}
    resp = requests.post(config.url + 'auth/register/v2', json = info)
    assert resp.status_code == InputError.code
    
def test_auth_register_duplicate_email(clear):
    info = {'email': 'monty@someplace.valid' , 'password': 'password' , 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info) # first request 
    info2 = {'email': 'monty@someplace.valid' , 'password': 'password' , 'name_first': 'monty', 'name_last': 'python'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # second request  
    assert resp2.status_code == InputError.code
    
