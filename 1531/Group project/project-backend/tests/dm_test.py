import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError

OK = 200

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_dm_create_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    dm_resp = requests.post(config.url + 'dm/create/v1', json = dm_info)
    assert dm_resp.status_code == OK

def test_dm_create_invalid_user(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token']
    dm_info = {'token': token, 'u_ids': [3]}
    dm_resp = requests.post(config.url + 'dm/create/v1', json = dm_info)
    assert dm_resp.status_code == InputError.code

def test_dm_invalid_token(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info)
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user
    dm_info = {'token': 'invalid_token', 'u_ids': [2]}
    dm_resp = requests.post(config.url + 'dm/create/v1', json = dm_info)
    assert dm_resp.status_code == AccessError.code

def test_dm_list_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.get(config.url + 'dm/list/v1', params = {"token": token})
    assert dm_info_resp.status_code == OK

def test_dm_remove_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.delete(config.url + 'dm/remove/v1', json = {"token": token, "dm_id": 1})
    assert dm_info_resp.status_code == OK

def test_dm_remove_invalid_dm(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.delete(config.url + 'dm/remove/v1', json = {"token": token, "dm_id": 100})
    assert dm_info_resp.status_code == InputError.code

def test_dm_remove_not_original_creator(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    reg_response2_data = reg_response2.json()
    token2 = reg_response2_data['token']
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.delete(config.url + 'dm/remove/v1', json = {"token": token2, "dm_id": 1})
    assert dm_info_resp.status_code == AccessError.code

def test_dm_details_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.get(config.url + 'dm/details/v1', params = {"token": token, "dm_id": 1})
    assert dm_info_resp.status_code == OK

def test_dm_details_invalid_dm(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.get(config.url + 'dm/details/v1', params = {"token": token, "dm_id": 100})
    assert dm_info_resp.status_code == InputError.code
  
def test_dm_details_invalid_user(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    info3 = {'email':'monty3@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response_user_3 = requests.post(config.url + 'auth/register/v2', json = info3) # registering a third user
    reg_response_user_3_data = reg_response_user_3.json()
    token_user_3 = reg_response_user_3_data['token']
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_info_resp = requests.get(config.url + 'dm/details/v1', params = {"token": token_user_3, "dm_id": 1})
    assert dm_info_resp.status_code == AccessError.code

def test_dm_leave_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_leave_info = {'token': token, 'dm_id': 1}
    dm_resp = requests.post(config.url + 'dm/leave/v1', json = dm_leave_info)
    assert dm_resp.status_code == OK
    
def test_dm_leave_invalid_dm(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_leave_info = {'token': token, 'dm_id': 100}
    dm_resp = requests.post(config.url + 'dm/leave/v1', json = dm_leave_info)
    assert dm_resp.status_code == InputError.code

def test_dm_leave_invalid_user(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    info3 = {'email':'monty3@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response_user_3 = requests.post(config.url + 'auth/register/v2', json = info3) # registering a third user
    reg_response_user_3_data = reg_response_user_3.json()
    token_user_3 = reg_response_user_3_data['token']    
    dm_info = {'token': token, 'u_ids': [2]}
    requests.post(config.url + 'dm/create/v1', json = dm_info) # creating a dm 
    dm_leave_info = {'token': token_user_3, 'dm_id': 1}
    dm_resp = requests.post(config.url + 'dm/leave/v1', json = dm_leave_info)
    assert dm_resp.status_code == AccessError.code
    
def test_dm_to_users_but_one_invalid(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    reg_response = requests.post(config.url + 'auth/register/v2', json = info)
    reg_response_data = reg_response.json()
    token = reg_response_data['token'] # token for first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering a second user 
    dm_info = {'token': token, 'u_ids': [2, -1]}
    dm_resp = requests.post(config.url + 'dm/create/v1', json = dm_info)
    assert dm_resp.status_code == InputError.code
