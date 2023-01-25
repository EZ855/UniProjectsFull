import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError

OK = 200 

@pytest.fixture
def clear():
    requests.delete(config.url + 'clear/v1')

def test_user_profile_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    u_id = resp_data["auth_user_id"]
    resp_users = requests.get(config.url + 'user/profile/v1', params = {"token": token, "u_id": u_id})
    assert resp_users.status_code == OK
    
def test_user_profile_invalid_token(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    u_id = resp_data["auth_user_id"]
    resp_users = requests.get(config.url + 'user/profile/v1', params = {"token": "invalid token", "u_id": u_id})
    assert resp_users.status_code == AccessError.code
    
def test_user_profile_invalid_id(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    resp_users = requests.get(config.url + 'user/profile/v1', params = {"token": token, "u_id": 9999})
    assert resp_users.status_code == InputError.code 
    
def test_user_profile_setname_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    resp_user = requests.put(config.url + 'user/profile/setname/v1', json = {"token": token, "name_first": "python", "name_last": "monty"})
    assert resp_user.status_code == OK
    
def test_user_profile_setname_invalid_first_name(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    name_first = "a" * 51
    resp_user = requests.put(config.url + 'user/profile/setname/v1', json = {"token": token, "name_first": name_first, "name_last": "monty"})
    assert resp_user.status_code == InputError.code

def test_user_profile_setname_invalid_last_name(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    name_last = "p" * 51
    resp_user = requests.put(config.url + 'user/profile/setname/v1', json = {"token": token, "name_first": "python", "name_last": name_last})
    assert resp_user.status_code == InputError.code

def test_user_profile_setemail_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_email = "python@mail.valid"
    resp_user = requests.put(config.url + 'user/profile/setemail/v1', json = {"token": token, "email": new_email})
    assert resp_user.status_code == OK

def test_user_profile_setemail_invalid_email(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_email = "python"
    resp_user = requests.put(config.url + 'user/profile/setemail/v1', json = {"token": token, "email": new_email})
    assert resp_user.status_code == InputError.code

def test_user_profile_setemail_unavailable_email(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info) # registering first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info2) # registering second user 
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_email = "monty@mail.valid"
    resp_user = requests.put(config.url + 'user/profile/setemail/v1', json = {"token": token, "email": new_email})
    assert resp_user.status_code == InputError.code

def test_user_profile_sethandle_functionality(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_handle = "newhandle"
    resp_user = requests.put(config.url + 'user/profile/sethandle/v1', json = {"token": token, "handle_str": new_handle})
    assert resp_user.status_code == OK

def test_user_profile_sethandle_invalid_length(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_handle = "py" * 11
    resp_user = requests.put(config.url + 'user/profile/sethandle/v1', json = {"token": token, "handle_str": new_handle})
    assert resp_user.status_code == InputError.code

def test_user_profile_sethandle_unavailable(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    requests.post(config.url + 'auth/register/v2', json = info) # registering first user 
    info2 = {'email':'monty2@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info2) # registering second user 
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_handle = "montypython"
    resp_user = requests.put(config.url + 'user/profile/sethandle/v1', json = {"token": token, "handle_str": new_handle})
    assert resp_user.status_code == InputError.code

def test_user_profile_sethandle_non_alphanumeric(clear):
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp_reg = requests.post(config.url + 'auth/register/v2', json = info)
    resp_data = resp_reg.json()
    token = resp_data["token"]
    new_handle = "!£%£!!" 
    resp_user = requests.put(config.url + 'user/profile/sethandle/v1', json = {"token": token, "handle_str": new_handle})
    assert resp_user.status_code == InputError.code
    
