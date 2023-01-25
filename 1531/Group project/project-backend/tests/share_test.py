import pytest
import requests
import json
from src import config
from src.error import InputError
from src.error import AccessError

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_functionality_channels(clear):

    message1 = "Hello World" 
    message2 = "Hello" 
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': message1}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    share_info = {"token": token, "og_message_id": message_id, "message": message2, "channel_id": channel_id, "dm_id": -1}
    requests.post(config.url + 'message/share/v1', json = share_info)
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    print(f"\" \n{message1}\n\" \n{message2}")
    assert msgs['messages'][0]['message'] == f"\" \n{message1}\n\" \n{message2}"
    assert msgs_json.status_code == 200

def test_functionality_dms(clear):

    message1 = "Hello World" 
    message2 = "Hello" 
    
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
    
    send_info = {'token': token,'dm_id': dm_id, 'message': message1}
    send_response = requests.post(config.url + 'message/senddm/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    share_info = {"token": token, "og_message_id": message_id, "message": message2, "channel_id": -1, "dm_id": dm_id}
    requests.post(config.url + 'message/share/v1', json = share_info)
    
    msg_info = {'token': token,'dm_id': dm_id, 'start': 0}
    msgs_json = requests.get(config.url + 'dm/messages/v1', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == f"\" \n{message1}\n\" \n{message2}"
    assert msgs_json.status_code == 200

    
def test_both_invalid(clear):
    
    message1 = "Hello World" 
    message2 = "Hello" 
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']

    
    send_info = {'token': token,'channel_id': channel_id, 'message': message1}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    share_info = {"token": token, "og_message_id": message_id, "message": message2, "channel_id": -1, "dm_id": -1}
    share_resp = requests.post(config.url + 'message/share/v1', json = share_info)
    assert share_resp.status_code == InputError.code
    
def test_channel_invalid_og(clear):
    message1 = "Hello World" 
    message2 = "Hello" 
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': message1}
    requests.post(config.url + 'message/send/v1', json = send_info)
    
    share_info = {"token": token, "og_message_id": 2, "message": message2, "channel_id": channel_id, "dm_id": -1}
    share_resp = requests.post(config.url + 'message/share/v1', json = share_info)
    assert share_resp.status_code == InputError.code

def test_channel_dm_invalid(clear):
    message1 = "Hello World" 
    message2 = "Hello" 
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': message1}
    requests.post(config.url + 'message/send/v1', json = send_info)
    
    share_info = {"token": token, "og_message_id": 1, "message": message2, "channel_id": 2, "dm_id": -1}
    share_resp = requests.post(config.url + 'message/share/v1', json = share_info)
    assert share_resp.status_code == InputError.code



def test_invalid_msg_len(clear):
    message1 = "Hello World" 
    message2 = "a" * 1001 
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': message1}
    requests.post(config.url + 'message/send/v1', json = send_info)
    
    share_info = {"token": token, "og_message_id": 1, "message": message2, "channel_id": channel_id, "dm_id": -1}
    share_resp = requests.post(config.url + 'message/share/v1', json = share_info)
    assert share_resp.status_code == InputError.code

def test_invalid_access(clear):
    
    message1 = "Hello World" 
    message2 = "Hello" 
    
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
    
    send_info = {'token': token,'channel_id': channel_id, 'message': message1}
    requests.post(config.url + 'message/send/v1', json = send_info)
    
    share_info = {"token": token2, "og_message_id": 1, "message": message2, "channel_id": channel_id, "dm_id": -1}
    share_resp = requests.post(config.url + 'message/share/v1', json = share_info)
    assert share_resp.status_code == AccessError.code
