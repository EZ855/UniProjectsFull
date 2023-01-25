import pytest
import requests
import json
from src import config

OK = 200

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

@pytest.fixture
def token_and_channel_id():
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = register_info) # registering user 
    login_info = {"email": "monty@mail.valid", "password": "password"} # json info 
    resp = requests.post(config.url + 'auth/login/v2', json = login_info) # log in request 
    token = resp.json()['token']
    response = requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    channel_id = int(response.json()['channel_id'])
    return {'token': token, 'channel_id': channel_id}


def test_user_stats_functionality_channel(clear, token_and_channel_id):
    info = {'token': token_and_channel_id['token']}
    response = requests.get(f'{config.url}/user/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['channels_joined'][0]['num_channels_joined'] == 1
    assert response.json()['involvement_rate'] == 1.0

def test_user_stats_functionality_dm(clear, token_and_channel_id):
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token_and_channel_id['token'], 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info)
    
    info = {'token': token_and_channel_id['token']}
    response = requests.get(f'{config.url}/user/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['dms_joined'][0]['num_dms_joined'] == 1
    assert response.json()['involvement_rate'] == 1.0

def test_user_stats_functionality_message_channel(clear):
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
    
    info = {'token': token}
    response = requests.get(f'{config.url}/user/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['messages_sent'][0]['num_messages_sent'] == 1
    assert response.json()['involvement_rate'] == 1.0

def test_user_stats_utilization_rate_denominator_zero(clear):
    register_info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = register_info) # registering user
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    
    info = {'token': token}
    response = requests.get(f'{config.url}/user/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['channels_joined'][0]['num_channels_joined'] == 0
    assert response.json()['involvement_rate'] == 0.0

def test_user_stats_utilization_rate_less_than_one(clear, token_and_channel_id):
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    requests.post(f'{config.url}/channels/create/v2', json={'name': 'Server1', 'token': token, 'is_public': False})
    
    info = {'token': token_and_channel_id['token']}
    response = requests.get(f'{config.url}/user/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['involvement_rate'] == 0.5
    
def test_users_stats_functionality_message_channel_remove(clear):
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
    requests.get(config.url + 'channel/messages/v2', params = msg_info)
    
    remove_info = {'token': token, 'message_id': 1}
    requests.delete(config.url + 'message/remove/v1', json = remove_info)
    
    info = {'token': token}
    response = requests.get(f'{config.url}/users/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['messages_exist'][0]['num_messages_exist'] == 0
    assert response.json()['utilization_rate'] == 1.0
    
def test_users_stats_functionality(clear, token_and_channel_id):
    info = {'token': token_and_channel_id['token']}
    response = requests.get(f'{config.url}/users/stats/v1', params=info)
    assert response.status_code == OK
    assert response.json()['channels_exist'][0]['num_channels_exist'] == 1
    assert response.json()['utilization_rate'] == 1.0
    
"""
def test_new_user_check():
    new_user_check()
    
    store = data_store.get()
    users = store['users']
    assert(users[1] == {'u_id': 2, 'stats': 1})
    assert(users[0] == {'u_id': 1, 'stats': {
                    'channels_joined': [0, 0],
                    'dms_joined': [0, 0], 
                    'messages_sent': [0, 0], 
                    'involvement_rate': 0 
                    }})
    

def test_involvement_calculation():
    store = data_store.get()
    users = store['users']
    
    for user in users:
        involvement_calculation(user)
    store = data_store.get()
    users = store['users']
    assert users == [{'u_id': 2, 'stats': 
                    {
                    'channels_joined': [2, 0],
                    'dms_joined': [0, 0], 
                    'messages_sent': [0, 0], 
                    'involvement_rate': 0.666
                    }}]


def test_channels_check():
    channels_check()
    store = data_store.get()
    users = store['users']
    assert(users[0] == {'u_id': 2, 'stats': {
                    'channels_joined': [1, some number],
                    'dms_joined': [0, 0], 
                    'messages_sent': [0, 0], 
                    'involvement_rate': 0.5
                    }})

def test_dms_check():
    dms_check()
    store = data_store.get()
    users = store['users']
    assert(users[0] == {'u_id': 2, 'stats': {
                    'channels_joined': [0, 0],
                    'dms_joined': [1, 0], 
                    'messages_sent': [0, 0], 
                    'involvement_rate': 0.5
                    }})

def test_stat_message_add():
    stat_message_add(2)
    store = data_store.get()
    users = store['users']
    assert(users[0] == {'u_id': 2, 'stats': {
                    'channels_joined': [0, 0],
                    'dms_joined': [0, 0], 
                    'messages_sent': [1, 0], 
                    'involvement_rate': 0.0
                    }})
"""