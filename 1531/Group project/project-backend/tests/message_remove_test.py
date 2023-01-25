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
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello World"}
    requests.post(config.url + 'message/send/v1', json = send_info)
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello World"
    assert msgs_json.status_code == 200
    
    remove_info = {'token': token, 'message_id': 1}
    requests.delete(config.url + 'message/remove/v1', json = remove_info)
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert len(msgs['messages']) == 0
    assert msgs_json.status_code == 200
    
def test_invalid_message(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    requests.post(config.url + 'channels/create/v2', json = channel_info)
    
    remove_info = {'token': token, 'message_id': 1}
    resp = requests.delete(config.url + 'message/remove/v1', json = remove_info)
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
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello World"}
    requests.post(config.url + 'message/send/v1', json = send_info)
    
    remove_info = {'token': token2, 'message_id': 1}
    resp = requests.delete(config.url + 'message/remove/v1', json = remove_info)
    assert resp.status_code == AccessError.code
    
