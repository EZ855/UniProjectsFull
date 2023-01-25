import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError

OK = 200

@pytest.fixture
def new():
    # clear 
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
    
    # creating channel 
    resp3 = requests.post(config.url + 'channels/create/v2', json={'name': 'channel', 'token': token, 'is_public': True})
    resp_data3 = resp3.json()
    channel_id = resp_data3['channel_id']
    
    #inviting user2 to channel 
    requests.post(config.url + 'channel/invite/v2', json={'token': token, 'channel_id': channel_id, 'u_id': u_id2})
    
    # creating dm 
    resp4 = requests.post(config.url + 'dm/create/v1', json={'token': token, 'u_ids': [u_id2]})
    resp_data4 = resp4.json()
    dm_id = resp_data4['dm_id']
    
    # obtaining dm name 
    dm_name_resp = requests.get(config.url + 'dm/details/v1', params={'token': token, 'dm_id': dm_id})
    dm_name_resp_data = dm_name_resp.json()
    dm_name = dm_name_resp_data['name']
    
    return {'token1': token, 'u_id1': u_id, 'token2': token2, 'u_id2': u_id2, 
            'channel_id': channel_id, 'channel_name': 'channel', 'dm_id': dm_id, 'dm_name': dm_name}
            
def test_functionality(new):
    # sending message 
    requests.post(config.url+ 'message/send/v1', json={'token': new['token2'], 'channel_id': new['channel_id'], 
                    'message': 'Hello. How are you?'}    
                 )   
    # sending dm
    requests.post(config.url + 'message/senddm/v1', json={'token': new['token2'], 'dm_id': new['dm_id'], 'message': f'Hello'})
    resp = requests.get(config.url + 'search/v1', params= {'token': new['token1'], 'query_str': "Hello"})
    assert resp.status_code == OK
    
def test_invalid_query_string_long(new):
    # sending message 
    requests.post(config.url+ 'message/send/v1', json={'token': new['token2'], 'channel_id': new['channel_id'], 
                    'message': 'Hello. How are you?'}    
                 )   
    # sending dm
    requests.post(config.url + 'message/senddm/v1', json={'token': new['token2'], 'dm_id': new['dm_id'], 'message': f'Hello'})
    query_str = 1001 * 'a'
    resp = requests.get(config.url + 'search/v1', params= {'token': new['token1'], 'query_str': query_str})
    assert resp.status_code == InputError.code

def test_invalid_query_string_short(new):
    # sending message 
    requests.post(config.url+ 'message/send/v1', json={'token': new['token2'], 'channel_id': new['channel_id'], 
                    'message': 'Hello. How are you?'}    
                 )   
    # sending dm
    requests.post(config.url + 'message/senddm/v1', json={'token': new['token2'], 'dm_id': new['dm_id'], 'message': f'Hello'})
    query_str = ''
    resp = requests.get(config.url + 'search/v1', params= {'token': new['token1'], 'query_str': query_str})
    assert resp.status_code == InputError.code    
