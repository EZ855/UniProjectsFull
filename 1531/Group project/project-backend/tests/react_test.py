import pytest
import requests
import json
from src import config
from src.error import InputError
from src.error import AccessError

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')
    
def test_functionality_react(clear):
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == 200
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello"
    assert msgs['messages'][0]['reacts'][0]['is_this_user_reacted'] == True
    assert 1 in msgs['messages'][0]['reacts'][0]['u_ids']
    assert msgs_json.status_code == 200

def test_functionality_react_dm(clear):
    
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
    
    send_info = {'token': token,'dm_id': dm_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/senddm/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == 200
    
    msg_info = {'token': token,'dm_id': dm_id, 'start': 0}
    msgs_json = requests.get(config.url + 'dm/messages/v1', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello"
    assert msgs['messages'][0]['reacts'][0]['is_this_user_reacted'] == True
    assert 1 in msgs['messages'][0]['reacts'][0]['u_ids']
    assert msgs_json.status_code == 200
    
def test_react_message_id_not_in_joined_channel(clear):
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
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token2, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == InputError.code


def test_invalid_react_id(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 2}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == InputError.code
    
def test_already_reacted(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == 200
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == InputError.code

def test_functionality_unreact(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == 200
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello"
    assert msgs['messages'][0]['reacts'][0]['is_this_user_reacted'] == True
    assert 1 in msgs['messages'][0]['reacts'][0]['u_ids']
    assert msgs_json.status_code == 200
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/unreact/v1', json = react_info)
    assert react_resp.status_code == 200
    
    msg_info = {'token': token,'channel_id': channel_id, 'start': 0}
    msgs_json = requests.get(config.url + 'channel/messages/v2', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello"
    assert msgs['messages'][0]['reacts'][0]['is_this_user_reacted'] == False
    assert 1 not in msgs['messages'][0]['reacts'][0]['u_ids']
    assert msgs_json.status_code == 200

def test_functionality_unreact_dm(clear):
    
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
    
    send_info = {'token': token,'dm_id': dm_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/senddm/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == 200
    
    msg_info = {'token': token,'dm_id': dm_id, 'start': 0}
    msgs_json = requests.get(config.url + 'dm/messages/v1', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello"
    assert msgs['messages'][0]['reacts'][0]['is_this_user_reacted'] == True
    assert 1 in msgs['messages'][0]['reacts'][0]['u_ids']
    assert msgs_json.status_code == 200
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/unreact/v1', json = react_info)
    assert react_resp.status_code == 200
    
    msg_info = {'token': token,'dm_id': dm_id, 'start': 0}
    msgs_json = requests.get(config.url + 'dm/messages/v1', params = msg_info)
    msgs = msgs_json.json()
    assert msgs['start'] == 0
    assert msgs['end'] == -1
    assert msgs['messages'][0]['message'] == "Hello"
    assert msgs['messages'][0]['reacts'][0]['is_this_user_reacted'] == False
    assert 1 not in msgs['messages'][0]['reacts'][0]['u_ids']
    assert msgs_json.status_code == 200

def test_unreact_message_id_not_in_joined_channel(clear):
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
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    unreact_info = {'token': token2, 'message_id': message_id, 'react_id': 1}
    unreact_resp = requests.post(config.url + 'message/unreact/v1', json = unreact_info)
    assert unreact_resp.status_code == InputError.code   
    
def test_unreact_react_id_Invalid(clear):
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/react/v1', json = react_info)
    assert react_resp.status_code == 200 
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 2}
    react_resp = requests.post(config.url + 'message/unreact/v1', json = react_info)
    assert react_resp.status_code == InputError.code
    
def test_no_react_to_unreact(clear):
    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    send_info = {'token': token,'channel_id': channel_id, 'message': "Hello"}
    send_response = requests.post(config.url + 'message/send/v1', json = send_info)
    send_response_data = send_response.json()
    message_id =  send_response_data['message_id']
    
    react_info = {'token': token, 'message_id': message_id, 'react_id': 1}
    react_resp = requests.post(config.url + 'message/unreact/v1', json = react_info)
    assert react_resp.status_code == InputError.code
