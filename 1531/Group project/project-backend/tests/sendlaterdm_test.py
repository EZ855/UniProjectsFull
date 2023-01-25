import pytest
import requests
import json
from src import config
from src.error import InputError
from src.error import AccessError
import time

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_functionality(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user

    dm_info = {'token': token, 'u_ids': [2]}
    dm_response = requests.post(config.url + 'dm/create/v1', json = dm_info)
    dm_response_data = dm_response.json()
    dm_id = dm_response_data['dm_id']
    
    send_info = {'token': token,'dm_id': dm_id, 'message': "Hello World", 'time_sent': int(time.time()) + 3}
    requests.post(config.url + 'message/sendlaterdm/v1', json = send_info)
    
    
    msg_info = {'token': token,'dm_id': dm_id, 'start': 0}
    msgs_json = requests.get(config.url + 'dm/messages/v1', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'] == []
    assert msgs_json.status_code == 200
    
    time.sleep(3)
    
    msg_info = {'token': token,'dm_id': dm_id, 'start': 0}
    msgs_json = requests.get(config.url + 'dm/messages/v1', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    print(msgs['messages'])
    assert msgs['messages'][0]['message'] == "Hello World"
    assert msgs_json.status_code == 200


def test_invalid_channel(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    dm_id = 1
    
    send_info = {'token': token,'dm_id': dm_id, 'message': "Hello World", 'time_sent': int(time.time()) + 10}
    resp = requests.post(config.url + 'message/sendlaterdm/v1', json = send_info)
    assert resp.status_code == InputError.code
    
def test_invalid_msg_length(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user

    dm_info = {'token': token, 'u_ids': [2]}
    dm_response = requests.post(config.url + 'dm/create/v1', json = dm_info)
    dm_response_data = dm_response.json()
    dm_id = dm_response_data['dm_id']
    
    send_info = {'token': token,'dm_id': dm_id, 'message': "a" * 1001, 'time_sent': int(time.time()) + 10}
    resp = requests.post(config.url + 'message/sendlaterdm/v1', json = send_info)
    assert resp.status_code == InputError.code

def test_invalid_time(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user

    dm_info = {'token': token, 'u_ids': [2]}
    dm_response = requests.post(config.url + 'dm/create/v1', json = dm_info)
    dm_response_data = dm_response.json()
    dm_id = dm_response_data['dm_id']
    
    send_info = {'token': token,'dm_id': dm_id, 'message': "hello", 'time_sent': int(time.time()) - 10}
    resp = requests.post(config.url + 'message/sendlaterdm/v1', json = send_info)
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
    
    send_info = {'token': token3,'dm_id': dm_id, 'message': "hello", 'time_sent': int(time.time()) + 10}
    resp = requests.post(config.url + 'message/sendlaterdm/v1', json = send_info)
    assert resp.status_code == AccessError.code
