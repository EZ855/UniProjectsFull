import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError
from src.auth import auth_register_v1
from src.channels import channels_create_v1 
from src.channel import channel_join_v1
from src.channel import channel_invite_v1
from src.channel import channel_details_v1
from src.other import clear_v1
from src.helpers import new_session_id, new_jwt, store_session_id, decode_jwt
from src.helpers import SECRET, check_valid_session, decode_jwt
OK = 200
@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_channel_join_v2_server(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']

    channel_join_info = {'token':token2, 'channel_id':channel_id}
    response_join = requests.post(config.url + 'channel/join/v2', json = channel_join_info)
    assert response_join.status_code == OK


def test_invalid_channel_join(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    channel_join_info = {'token':token, 'channel_id': 1}
    response_join = requests.post(config.url + 'channel/join/v2', json = channel_join_info)
    assert response_join.status_code == InputError.code

def test_multiple_channels_join(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    channel_info = {'token': token, 'name': "temp_name2", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']

    channel_join_info = {'token':token2, 'channel_id':channel_id}
    response_join = requests.post(config.url + 'channel/join/v2', json = channel_join_info)
    assert response_join.status_code == OK
    
def test_not_global_owner_and_private(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': False}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']

    channel_join_info = {'token':token2, 'channel_id':channel_id}
    response_join = requests.post(config.url + 'channel/join/v2', json = channel_join_info)
    assert response_join.status_code == AccessError.code
    
def test_already_member(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': False}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    channel_join_info = {'token':token, 'channel_id':channel_id}
    response_join = requests.post(config.url + 'channel/join/v2', json = channel_join_info)
    assert response_join.status_code == InputError.code
    
def test_channel_invite_v2_server(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    u_id = reg_response_data2['auth_user_id']
    
    channel_invite_info = {'token':token, 'channel_id':channel_id, 'u_id':u_id}    
    response_invite = requests.post(config.url + 'channel/invite/v2', json = channel_invite_info)
    assert response_invite.status_code == OK
    
def test_invalid_channel_invite(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    u_id = reg_response_data2['auth_user_id']
    
    channel_invite_info = {'token':token, 'channel_id': 1, 'u_id':u_id}    
    response_invite = requests.post(config.url + 'channel/invite/v2', json = channel_invite_info)
    assert response_invite.status_code == InputError.code
    
def test_invalid_user_id_invite(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']

    channel_invite_info = {'token':token, 'channel_id':channel_id, 'u_id': 2}    
    response_invite = requests.post(config.url + 'channel/invite/v2', json = channel_invite_info)
    assert response_invite.status_code == InputError.code
    
def test_auth_user_not_member_invite(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
    
    register_info3 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    requests.post(config.url + 'auth/register/v2', json = register_info3) # registering user
    
    channel_invite_info = {'token':token2, 'channel_id':channel_id, 'u_id': 2}    
    response_invite = requests.post(config.url + 'channel/invite/v2', json = channel_invite_info)
    assert response_invite.status_code == AccessError.code
    
def test_invite_already_member_invite(clear):    
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    register_info2 = {'email':'abc@mail.valid' , 'password': 'password123', 'name_first': 'mark', 'name_last': 'great'}
    requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    
    channel_invite_info = {'token':token, 'channel_id':channel_id, 'u_id':1}    
    response_invite = requests.post(config.url + 'channel/invite/v2', json = channel_invite_info)
    assert response_invite.status_code == InputError.code
    
    

