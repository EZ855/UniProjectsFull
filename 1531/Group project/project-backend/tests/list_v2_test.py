import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError


OK = 200

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

    
def test_one_channel(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "first", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info)

    
    info = {'token': token}
    resp = requests.get(config.url + 'channels/list/v2', params = info)
    assert resp.status_code == 200
    resp_data = resp.json()
    assert resp_data == {'channels':[{'channel_id': 1, 'name': 'first'}]}
    
    
def test_two_channel(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "first", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info)

    
    channel_info2 = {'token': token, 'name': "second", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info2)

    
    info = {'token': token}
    resp = requests.get(config.url + 'channels/list/v2', params = info)
    assert resp.status_code == 200
    resp_data = resp.json()
    assert resp_data == {'channels':[{'channel_id': 1, 'name': 'first'}, {'channel_id': 2, 'name': 'second'}]}
    
def test_only_accessible(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    channel_info = {'token': token, 'name': "first", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info)

    
    channel_info2 = {'token': token, 'name': "second", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info2)

    channel_info3 = {'token': token2, 'name': "third", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info3)
    
    info = {'token': token}
    resp = requests.get(config.url + 'channels/list/v2', params = info)
    assert resp.status_code == 200
    resp_data = resp.json()
    assert resp_data == {'channels':[{'channel_id': 1, 'name': 'first'}, {'channel_id': 2, 'name': 'second'}]}
