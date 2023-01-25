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
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello World", 'time_sent': int(time.time()) + 3}
    requests.post(config.url + 'message/sendlater/v1', json = send_info)
    
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'] == []
    assert msgs_json.status_code == 200
    
    time.sleep(3)
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
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

    channel_id = 1
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello World", 'time_sent': int(time.time()) + 10}
    resp = requests.post(config.url + 'message/sendlater/v1', json = send_info)
    assert resp.status_code == InputError.code
    
def test_invalid_msg_length(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "a" * 1001, 'time_sent': int(time.time()) + 10}
    resp = requests.post(config.url + 'message/sendlater/v1', json = send_info)
    assert resp.status_code == InputError.code

def test_invalid_time(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "hello", 'time_sent': int(time.time()) - 10}
    resp = requests.post(config.url + 'message/sendlater/v1', json = send_info)
    assert resp.status_code == InputError.code
    
def test_invalid_access(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    register_info2 = {'email':'monty2@mail.valid' , 'password': 'password2', 'name_first': 'monty2', 'name_last': 'python2'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token2,'channel_id': channel_id, 'message': "hello", 'time_sent': int(time.time()) + 10}
    resp = requests.post(config.url + 'message/sendlater/v1', json = send_info)
    assert resp.status_code == AccessError.code
