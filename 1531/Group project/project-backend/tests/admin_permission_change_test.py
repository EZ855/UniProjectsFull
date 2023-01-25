import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError

OK = 200

@pytest.fixture
def new():
    requests.delete(config.url + 'clear/v1')
    # registering user1
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info) # registering user 
    resp_data = resp.json()
    token = resp_data["token"]
    u_id = resp_data["auth_user_id"]
    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering user 
    resp_data2 = resp2.json()
    token2 = resp_data2["token"]
    u_id2 = resp_data2["auth_user_id"]
    
    #registering user3
    info3 = {'email':'montypython@mail.valid' , 'password': 'password', 'name_first': 'montypython', 'name_last': 'monty'}
    resp3 = requests.post(config.url + 'auth/register/v2', json = info3) # registering user 
    resp_data3 = resp3.json()
    token3 = resp_data3["token"]
    u_id3 = resp_data3["auth_user_id"]
    
    
    return {'token1': token, 'u_id1': u_id, 'token2': token2, 'u_id2': u_id2, 'token3': token3, 'u_id3': u_id3}
    
def test_functionality(new):
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token1'], 'u_id': new['u_id2'], 'permission_id': 1})
    assert resp.status_code == OK

def test_remove_global_owner(new):
    # make third user into global owner
    requests.post(config.url + 'admin/userpermission/change/v1', json={'token': new['token1'], 'u_id': new['u_id3'], 'permission_id': 1})
    
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token1'], 'u_id': new['u_id3'], 'permission_id': 2})
    assert resp.status_code == OK
    
def test_change_owner_to_owner(new):
    # make third user into global owner
    requests.post(config.url + 'admin/userpermission/change/v1', json={'token': new['token1'], 'u_id': new['u_id3'], 'permission_id': 1})
    
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token1'], 'u_id': new['u_id3'], 'permission_id': 1})
    assert resp.status_code == OK
    
def test_invalid_user(new):
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token1'], 'u_id': 10, 'permission_id': 1})
    assert resp.status_code == InputError.code
    
def test_only_global_owner(new):
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token1'], 'u_id': new['u_id1'], 'permission_id': 2})
    assert resp.status_code == InputError.code
    
def test_invalid_permission(new):
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token1'], 'u_id': new['u_id2'], 'permission_id': 0})
    assert resp.status_code == InputError.code
    
def test_not_global_owner(new):
    resp = requests.post(config.url + 'admin/userpermission/change/v1', json = {"token": new['token2'], 'u_id': new['u_id2'], 'permission_id': 1})
    assert resp.status_code == AccessError.code
