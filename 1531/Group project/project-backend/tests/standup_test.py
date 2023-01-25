import pytest
from src.data_store import data_store
from src.error import InputError, AccessError
from datetime import timezone
import datetime
import threading
import time
from src.other import clear_v1
from src.auth import auth_register_v1
from src.channels import channels_create_v1 
from src.channel import channel_join_v1
import requests
import json
from src import config
from src.message import message_send_v1
from src.standup import standup_start_v1
from src.standup import buffer_detail, standup_active_v1, standup_send_v1,standup_finish,auth_user_handle_str

OK =200
@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_standup_start_v1_Errors(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']

    register_info2 = {'email':'123321@qq.com' , 'password': 'new123321', 'name_first': 'Mark', 'name_last': 'Li'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    standup_info = {'token':token, 'channel_id':channel_id, 'length':-10}
    standup_start_response = requests.post(config.url + 'standup/start/v1', json = standup_info)
    assert standup_start_response.status_code == InputError.code

    standup_info2 = {'token':token, 'channel_id':1000, 'length':10}
    standup_start_response2 = requests.post(config.url + 'standup/start/v1', json = standup_info2)
    assert standup_start_response2.status_code == InputError.code

    standup_info3 = {'token':token, 'channel_id':channel_id, 'length':3}
    requests.post(config.url + 'standup/start/v1', json = standup_info3)

    standup_info4 = {'token':token, 'channel_id':channel_id, 'length':3}
    standup_start_response4 = requests.post(config.url + 'standup/start/v1', json = standup_info4)
    assert standup_start_response4.status_code == InputError.code

    time.sleep(3)

    standup_info5 = {'token':token2, 'channel_id':channel_id, 'length':3}
    standup_start_response5 = requests.post(config.url + 'standup/start/v1', json = standup_info5)
    assert standup_start_response5.status_code == AccessError.code
    

def test_standup_active_v1(clear):

    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    register_info2 = {'email':'123321@qq.com' , 'password': 'new123321', 'name_first': 'Mark', 'name_last': 'Li'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    standup_info = {'token':token, 'channel_id':channel_id, 'length':10}
    requests.post(config.url + 'standup/start/v1', json = standup_info)
    
    standup_active_info = {'token':token, 'channel_id':1000}
    standup_active_response = requests.get(config.url + 'standup/active/v1', params = standup_active_info)
    assert standup_active_response.status_code == InputError.code
    
    standup_active_info2 = {'token':token2, 'channel_id':channel_id}
    standup_active_response2 = requests.get(config.url + 'standup/active/v1', params = standup_active_info2)
    assert standup_active_response2.status_code == AccessError.code
    
    #inputerror and accesserror can not pss
    time.sleep(10)
 
    
def test_standup_send_v1(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    register_info2 = {'email':'123321@qq.com' , 'password': 'new123321', 'name_first': 'Mark', 'name_last': 'Li'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = register_info2) # registering user
    reg_response_data2 = reg_response2.json()
    token2 = reg_response_data2['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    standup_info = {'token':token, 'channel_id':channel_id, 'length':5}
    requests.post(config.url + 'standup/start/v1', json = standup_info)
    
    standup_send_info = { 'token':token, 'channel_id':1000, 'message':'Hello'}
    standup_send_response = requests.post(config.url + 'standup/send/v1', json = standup_send_info)
    assert standup_send_response.status_code == InputError.code
    #assert buffer != []
    
    standup_send_info2 = { 'token':token, 'channel_id':channel_id, 'message':''}
    standup_send_response2 = requests.post(config.url + 'standup/send/v1', json = standup_send_info2)
    assert standup_send_response2.status_code == InputError.code
    
    standup_send_info3 = { 'token':token2, 'channel_id':channel_id, 'message':'How are you'}
    standup_send_response3 = requests.post(config.url + 'standup/send/v1', json = standup_send_info3)
    assert standup_send_response3.status_code == AccessError.code
    time.sleep(5)

def test_standup_start_v1_server(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']

    standup_info = {'token':token, 'channel_id':channel_id, 'length':5}
    standup_response = requests.post(config.url + 'standup/start/v1', json = standup_info)
    assert standup_response.status_code == OK
    time.sleep(5)

def test_standup_active_v1_server(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = int(ch_response_data['channel_id'])
    
    standup_info = {'token':token, 'channel_id':channel_id, 'length':10}
    requests.post(config.url + 'standup/start/v1', json = standup_info)
    
    standup_active_info = {'token':token, 'channel_id':channel_id}
    standup_active_response = requests.get(config.url + 'standup/active/v1', params = standup_active_info)
    
    assert standup_active_response.status_code == OK
    time.sleep(10)


def test_standup_send_v1_server(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
 
    channel_info = {'token': token, 'name': "temp_name", 'is_public': True}
    ch_response = requests.post(config.url + 'channels/create/v2', json = channel_info)
    ch_response_data = ch_response.json()
    channel_id = ch_response_data['channel_id']
    
    standup_info = {'token':token, 'channel_id':channel_id, 'length':10}
    requests.post(config.url + 'standup/start/v1', json = standup_info)
    
    standup_send_info = {'token':token, 'channel_id':channel_id, 'message':'Hello,there'}

    standup_send_response = requests.post(config.url + 'standup/send/v1', json = standup_send_info)
    assert standup_send_response.status_code == OK



