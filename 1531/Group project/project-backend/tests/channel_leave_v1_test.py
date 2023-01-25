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
from src.channel import channel_leave_v1
from src.other import clear_v1
OK = 200
@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')


def test_channel_leave_server_InputError(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']


    channel_leave_info = {'token':token, 'channel_id':100}
    response_leave = requests.post(config.url + 'channel/leave/v1', json = channel_leave_info)
    assert response_leave.status_code == InputError.code


def test_channel_leave_server_AccessError(clear):
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
    
    
    channel_leave_info = {'token':token2, 'channel_id':channel_id}
    response_leave = requests.post(config.url + 'channel/leave/v1', json = channel_leave_info)
    assert response_leave.status_code == AccessError.code
    
def test_channel_leave_functionality(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']  
    
    channel_info = {'token': token, 'name': "temp_name2", 'is_public': True}
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

    channel_leave_info = {'token':token2, 'channel_id':channel_id}
    response_leave = requests.post(config.url + 'channel/leave/v1', json = channel_leave_info)
    assert response_leave.status_code == OK

def test_leave_functionality_owner(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']  
    
    channel_info = {'token': token, 'name': "temp_name2", 'is_public': True}
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

    channel_leave_info = {'token':token, 'channel_id':channel_id}
    response_leave = requests.post(config.url + 'channel/leave/v1', json = channel_leave_info)
    assert response_leave.status_code == OK


