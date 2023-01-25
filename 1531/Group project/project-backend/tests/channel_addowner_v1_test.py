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
    
    # creating channel1 
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel1', 'token': token2, 'is_public': False})
    resp_data3 = resp3.json()
    channel_id1 = resp_data3['channel_id']
    
    # creating channel2 
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel2', 'token': token, 'is_public': True})
    resp_data3 = resp3.json()
    channel_id2 = resp_data3['channel_id']
    
    # creating channel3 
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel3', 'token': token2, 'is_public': True})
    resp_data3 = resp3.json()
    channel_id3 = resp_data3['channel_id']
    
    # creating channel4
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel4', 'token': token2, 'is_public': True})
    resp_data3 = resp3.json()
    channel_id4 = resp_data3['channel_id']
    
    #inviting user2 to channel 2
    requests.post(config.url + 'channel/invite/v2', json={'token': token, 'channel_id': channel_id2, 'u_id': u_id2})
    
    #inviting user3 to channel 1
    requests.post(config.url + 'channel/invite/v2', json={'token': token2, 'channel_id': channel_id1, 'u_id': u_id3})
    
    #inviting user3 to channel 3
    requests.post(config.url + 'channel/invite/v2', json={'token': token2, 'channel_id': channel_id3, 'u_id': u_id3})
    
    #inviting user1 to channel 4
    requests.post(config.url + 'channel/invite/v2', json={'token': token2, 'channel_id': channel_id4, 'u_id': u_id})
       
    return {'token1': token, 'u_id1': u_id, 'token2': token2, 'u_id2': u_id2,'token3': token3, 'u_id3': u_id3, 'channel_id1': channel_id1, 'channel_id2': channel_id2, 'channel_id3': channel_id3, 'channel_id4': channel_id4}

    
def test_channel_addowner_v1_functionality(new):  
    
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id2'], 'u_id': new['u_id2']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == OK
    
    detail_info = {'token': new['token1'], 'channel_id': new['channel_id2']}
    detail_resp = requests.get(config.url + 'channel/details/v2', params=detail_info)
    detail_resp_data = detail_resp.json()
    
    assert detail_resp_data['owner_members'][0]['u_id'] == 1
    assert detail_resp_data['owner_members'][1]['u_id'] == 2
    
def test_invalid_channel(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': 200, 'u_id': new['u_id2']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == InputError.code 
    
def test_invalid_user_id(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id2'], 'u_id': 150}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == InputError.code 

def test_user_id_not_joined(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id2'], 'u_id': new['u_id3']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == InputError.code 
        
def test_already_owner(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id2'], 'u_id': new['u_id1']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == InputError.code   

def test_no_owner_perms(new):
    channel_addowner_info = {'token':new['token2'], 'channel_id': new['channel_id2'], 'u_id': new['u_id2']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == AccessError.code   

def test_non_member_can_not_add(new):
    channel_addowner_info = {'token':new['token3'], 'channel_id': new['channel_id2'], 'u_id': new['u_id2']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == AccessError.code  
    
def test_global_owner_non_member_cant_addowner_private(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id1'], 'u_id': new['u_id3']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == AccessError.code
    
def test_global_owner_non_member_cant_addowner_public(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id3'], 'u_id': new['u_id3']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == AccessError.code  
    
def test_global_owner_is_member_can_addowner(new):
    channel_addowner_info = {'token':new['token1'], 'channel_id': new['channel_id4'], 'u_id': new['u_id1']}
    response_addowner = requests.post(config.url + 'channel/addowner/v1', json = channel_addowner_info)
    assert response_addowner.status_code == OK
    
    
        
