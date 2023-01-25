import pytest
import requests
import json
from src import config
from src.error import InputError
from src.error import AccessError


@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_functionality(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
    
    register_info3 = {'email':'monty3@mail.valid' , 'password': 'password3', 'name_first': 'monty3', 'name_last': 'python3'}
    reg_response3 = requests.post(config.url + 'auth/register/v2', json = register_info3) # registering user
    reg_response_data3 = reg_response3.json()
    token3 = reg_response_data3['token']
 
    dm_info = {'token': token, 'u_ids': [2,3]}
    dm_response = requests.post(config.url + 'dm/create/v1', json = dm_info)
    dm_response_data = dm_response.json()
    dm_id = dm_response_data['dm_id']
    
    
    info = {'token': token2,'dm_id': dm_id, 'start': 0}
    resp = requests.get(config.url + 'dm/messages/v1', params = info)
    assert resp.status_code == 200
    
    info = {'token': token3,'dm_id': dm_id, 'start': 0}
    resp = requests.get(config.url + 'dm/messages/v1', params = info)
    assert resp.status_code == 200
    
def test_invalid_dm(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    info = {'token': token,'dm_id': 1, 'start': 0}
    resp = requests.get(config.url + 'dm/messages/v1', params = info)
    assert resp.status_code == InputError.code

def test_invalid_start(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    dm_info = {'token': token, 'u_ids': [2]}
    dm_response = requests.post(config.url + 'dm/create/v1', json = dm_info)
    dm_response_data = dm_response.json()
    dm_id = dm_response_data['dm_id']    
    
    info = {'token': token2,'dm_id': dm_id, 'start': 100}
    resp = requests.get(config.url + 'dm/messages/v1', params = info)
    assert resp.status_code == InputError.code

def test_invalid_access(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    
    register_info3 = {'email':'monty3@mail.valid' , 'password': 'password3', 'name_first': 'monty3', 'name_last': 'python3'}
    reg_response3 = requests.post(config.url + 'auth/register/v2', json = register_info3) # registering user
    reg_response_data3 = reg_response3.json()
    token3 = reg_response_data3['token']
 
    dm_info = {'token': token, 'u_ids': [2]}
    dm_response = requests.post(config.url + 'dm/create/v1', json = dm_info)
    dm_response_data = dm_response.json()
    dm_id = dm_response_data['dm_id']
    
    
    info = {'token': token3,'dm_id': dm_id, 'start': 0}
    resp = requests.get(config.url + 'dm/messages/v1', params = info)
    assert resp.status_code == AccessError.code
    
    
