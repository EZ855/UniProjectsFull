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
    
    # creating channel 
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel', 'token': token, 'is_public': True})
    resp_data3 = resp3.json()
    channel_id = resp_data3['channel_id']
    
    #inviting user2 to channel 
    requests.post(config.url + 'channel/invite/v2', json={'token': token, 'channel_id': channel_id, 'u_id': u_id2})
    
    # creating dm 
    resp4 = requests.post(config.url + 'dm/create/v1', json={'token': token, 'u_ids': [u_id2]})
    resp_data4= resp4.json()
    dm_id = resp_data4['dm_id']
    
    # sending message
    requests.post(config.url+ 'message/send/v1', json={'token': token2, 'channel_id': channel_id, 'message': 'Hello'})
    
    # sending dm
    requests.post(config.url + 'message/senddm/v1', json={'token': token2, 'dm_id': dm_id, 'message': 'Hello'})
       
    return {'token1': token, 'u_id1': u_id, 'token2': token2, 'u_id2': u_id2,'token3': token3, 'u_id3': u_id3, 'channel_id': channel_id, 'dm_id': dm_id}
    
def test_functionality(new):
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token1'], 'u_id': new['u_id2']})
    assert resp.status_code == OK
    
def test_channel_owner_remove(new):

    # make third user into global owner
    requests.post(config.url + 'admin/userpermission/change/v1', json={'token': new['token1'], 'u_id': new['u_id3'], 'permission_id': 1})    
    
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token3'], 'u_id': new['u_id1']})
    assert resp.status_code == OK    
    
def test_invalid_user(new):
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token1'], 'u_id': 10})
    assert resp.status_code == InputError.code
    
def test_only_global_owner(new):
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token1'], 'u_id': new['u_id1']})
    assert resp.status_code == InputError.code

def test_not_global_owner(new):
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token2'], 'u_id': new['u_id2']})
    assert resp.status_code == AccessError.code
    
def test_user_cant_do_anything(new):
    requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token1'], 'u_id': new['u_id2']})
    response_logout = requests.post(config.url + 'auth/logout/v1', json = {"token": new['token2']}) # log out request 
    assert response_logout.status_code == AccessError.code 
    
def test_messages_after_user_removal(new):
    requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token1'], 'u_id': new['u_id2']})
    resp = requests.get(config.url + 'channel/messages/v2', params={'token': new['token1'], 'channel_id': new['channel_id'],'start': 0})
    resp_data = resp.json()
    assert resp_data['messages'][0]['message'] == "Removed user"
    
def test_dm_after_user_removal(new):
    requests.delete(config.url + 'admin/user/remove/v1', json = {"token": new['token1'], 'u_id': new['u_id2']})
    resp = requests.get(config.url + 'dm/messages/v1', params={'token': new['token1'], 'dm_id': new['dm_id'],'start': 0})
    resp_data = resp.json()
    assert resp_data['messages'][0]['message'] == "Removed user"

def test_no_channels_or_dm():
    requests.delete(config.url + 'clear/v1')
    # registering user1
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info) # registering user 
    resp_data = resp.json()
    token = resp_data["token"]
    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering user 
    resp_data2 = resp2.json()
    u_id2 = resp_data2["auth_user_id"]
    
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": token, 'u_id': u_id2})
    assert resp.status_code == OK

def test_not_in_any_channels():
    requests.delete(config.url + 'clear/v1')
    # registering user1
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info) # registering user 
    resp_data = resp.json()
    token = resp_data["token"]
    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering user 
    resp_data2 = resp2.json()
    u_id2 = resp_data2["auth_user_id"]
    
    #registering user3
    info3 = {'email':'montypython@mail.valid' , 'password': 'password', 'name_first': 'montypython', 'name_last': 'monty'}
    requests.post(config.url + 'auth/register/v2', json = info3) # registering user 

    
    # creating channel 
    requests.post(config.url + 'channels/create/v2', json={'name': 'channel', 'token': token, 'is_public': True})
    
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": token, 'u_id': u_id2})
    assert resp.status_code == OK

def test_channel_no_message():
    requests.delete(config.url + 'clear/v1')
    # registering user1
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info) # registering user 
    resp_data = resp.json()
    token = resp_data["token"]
    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering user 
    resp_data2 = resp2.json()
    u_id2 = resp_data2["auth_user_id"]
    
    #registering user3
    info3 = {'email':'montypython@mail.valid' , 'password': 'password', 'name_first': 'montypython', 'name_last': 'monty'}
    requests.post(config.url + 'auth/register/v2', json = info3) # registering user 

    
    # creating channel 
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel', 'token': token, 'is_public': True})
    resp_data3 = resp3.json()
    channel_id = resp_data3['channel_id']
    
    #inviting user2 to channel 
    requests.post(config.url + 'channel/invite/v2', json={'token': token, 'channel_id': channel_id, 'u_id': u_id2})
    
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": token, 'u_id': u_id2})
    assert resp.status_code == OK
    
def test_not_in_any_dms():
    requests.delete(config.url + 'clear/v1')
    # registering user1
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info) # registering user 
    resp_data = resp.json()
    token = resp_data["token"]
    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering user 
    resp_data2 = resp2.json()
    u_id2 = resp_data2["auth_user_id"]
    
    #registering user3
    info3 = {'email':'montypython@mail.valid' , 'password': 'password', 'name_first': 'montypython', 'name_last': 'monty'}
    resp3 = requests.post(config.url + 'auth/register/v2', json = info3) # registering user 
    resp_data3 = resp3.json()
    u_id3 = resp_data3["auth_user_id"]
    
    # creating dm 
    requests.post(config.url + 'dm/create/v1', json={'token': token, 'u_ids': [u_id3]})
    
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": token, 'u_id': u_id2})
    assert resp.status_code == OK   

def test_no_messages_in_any_dms():
    requests.delete(config.url + 'clear/v1')
    # registering user1
    info = {'email':'monty@mail.valid' , 'password': 'password', 'name_first': 'monty', 'name_last': 'python'}
    resp = requests.post(config.url + 'auth/register/v2', json = info) # registering user 
    resp_data = resp.json()
    token = resp_data["token"]

    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    requests.post(config.url + 'auth/register/v2', json = info2) # registering user 

    
    #registering user3
    info3 = {'email':'montypython@mail.valid' , 'password': 'password', 'name_first': 'montypython', 'name_last': 'monty'}
    resp3 = requests.post(config.url + 'auth/register/v2', json = info3) # registering user 
    resp_data3 = resp3.json()
    u_id3 = resp_data3["auth_user_id"]
    
    # creating dm 
    requests.post(config.url + 'dm/create/v1', json={'token': token, 'u_ids': [u_id3]})

    
    resp = requests.delete(config.url + 'admin/user/remove/v1', json = {"token": token, 'u_id': u_id3})
    assert resp.status_code == OK       
