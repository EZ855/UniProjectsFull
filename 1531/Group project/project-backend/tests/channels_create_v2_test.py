import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError

OK = 200

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

@pytest.fixture
def token():
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "monty@mail.valid", "password": "password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    token = resp.json()['token']
    return token

def test_create_server(clear, token):
    response = requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    assert response.status_code == OK
    
def test_channels_create_long_name(clear, token):
    resp = requests.post(f'{config.url}/channels/create/v2', json={'name': '012345678901234567890', 'token': token, 'is_public': False})
    assert resp.status_code == InputError.code

def test_channels_create_short_name(clear, token):
    resp = requests.post(f'{config.url}/channels/create/v2', json={'name': '', 'token': token, 'is_public': False})
    assert resp.status_code == InputError.code

def test_channels_create_invalid_user_id(clear, token):
    resp = requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': 'asdf', 'is_public': False})
    assert resp.status_code == AccessError.code
    
"""
def test_channels_create_list_all(clear, token):
    requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    request = requests.post(f'{config.url}/channels/listall/v2', json={'token': token})
    assert response.status_code == OK
    assert response.json = {'channels': [{'channel_id': 1, 'name': 'Server1'}]}
    
def test_channels_create_list(clear, token):
    requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    request = requests.post(f'{config.url}/channels/list/v2', json={'token': token})
    assert response.status_code == OK
    assert response.json = {'channels': [{'channel_id': 1, 'name': 'Server1'}]}
    
    
def test_channels_create_join(clear, token):
    resp = requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    channel_id = resp.json()['channel_id']
    
    register_info = {'email':'1monty@mail.valid' , 'password': '1password', 'name_first': '1monty', 'name_last': '1python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "1monty@mail.valid", "password": "1password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    1token = resp.json()['token']
    
    response = requests.get(f'{config.url}/channel/join/v2', json={'token': 1token, 'channel_id': channel_id})
    assert response.status_code == OK
    
"""