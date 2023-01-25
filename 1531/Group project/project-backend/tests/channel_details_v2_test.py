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

@pytest.fixture
def token_and_channel_id():
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "monty@mail.valid", "password": "password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    token = resp.json()['token']
    response = requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    channel_id = int(response.json()['channel_id'])
    return {'token': token, 'channel_id': channel_id}

def test_channel_details_functionality(clear, token_and_channel_id):
    info = {'token': token_and_channel_id['token'], 'channel_id': token_and_channel_id['channel_id']}
    response = requests.get(f'{config.url}/channel/details/v2', params=info)
    assert response.status_code == OK

def test_channels_details_invalid_channel_id(clear, token_and_channel_id):
    info = {'token': token_and_channel_id['token'], 'channel_id': 2000}
    response = requests.get(f'{config.url}/channel/details/v2', params=info)
    assert response.status_code == InputError.code

def test_channels_details_not_a_member(clear, token_and_channel_id):
    register_info = {'email':'1monty@mail.valid' , 'password': '1password', 'name_first': '1monty', 'name_last': '1python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "1monty@mail.valid", "password": "1password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    token1 = resp.json()['token']
    
    info = {'token': token1, 'channel_id': token_and_channel_id['channel_id']}
    response = requests.get(f'{config.url}/channel/details/v2', params=info)
    assert response.status_code == AccessError.code

def test_channels_details_no_channels(clear, token):
    info = {'token': token, 'channel_id': 1}
    response = requests.get(f'{config.url}/channel/details/v2', params=info)
    assert response.status_code == InputError.code
"""
def test_channels_details_jwt_error(clear):
    token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    info = {'token': token, 'channel_id': 'asdf'}
    response = requests.get(f'{config.url}/channel/details/v2', params=info)
    assert response.status_code == AccessError.code
"""