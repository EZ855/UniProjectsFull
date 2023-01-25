import pytest
import requests
import json
from src import config
from src.error import InputError, AccessError
import time 

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
    
    # obtaining user1 handle 
    resp5 = requests.get(config.url + 'user/profile/v1', params={"token": token, "u_id": u_id})
    resp_data5 = resp5.json()
    user_handle = resp_data5['user']['handle_str']
    
    # obtaining user2 handle 
    resp6 = requests.get(config.url + 'user/profile/v1', params={"token": token2, "u_id": u_id2})
    resp_data6 = resp6.json()
    user_handle2 = resp_data6['user']['handle_str']
    
    return {'token1': token, 'u_id1': u_id, 'user_handle1': user_handle, 'token2': token2, 'u_id2': u_id2, 
            'user_handle2': user_handle2, 'channel_id': channel_id, 'channel_name': 'channel', 'dm_id': dm_id, 'dm_name': dm_name}
    
def test_functionality(new):
    user_handle = new['user_handle1']
     # sending message
    requests.post(config.url+ 'message/send/v1', json={'token': new['token2'], 'channel_id': new['channel_id'], 'message': 'Hello{user_handle}'})   
    # sending dm
    requests.post(config.url + 'message/senddm/v1', json={'token': new['token2'], 'dm_id': new['dm_id'], 'message': f'Hello {user_handle}'})
    resp = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    assert resp.status_code == OK

def test_channel_message_notification_format(new): 
    user_handle2 = new['user_handle2']
    # tagging user2 in channel message 
    requests.post(config.url+ 'message/send/v1', 
                  json={'token': new['token2'], 'channel_id': new['channel_id'], 'message': f'Hello @{user_handle2}'}
                 )   
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token2']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} tagged you in channel: Hello @{user_handle2}"
    
    
def test_dm_message_notification_format(new): 
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # tagging user2 in dm message 
    requests.post(config.url+ 'message/senddm/v1', 
                  json={'token': new['token1'], 'dm_id': new['dm_id'], 'message': f'Hello @{user_handle2}'}
                 )   
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token2']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle} tagged you in {new['dm_name']}: Hello @{user_handle2}"
    
def test_channel_message_notification_creator(new):
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # tagging user1 in channel message 
    requests.post(config.url+ 'message/send/v1', 
                  json={'token': new['token2'], 'channel_id': new['channel_id'], 'message': f'Hello @{user_handle}'}
                 )  
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} tagged you in channel: Hello @{user_handle}" 
    
def test_dm_message_notification_creator(new):
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # tagging user1 in dm message 
    requests.post(config.url+ 'message/senddm/v1', 
                  json={'token': new['token2'], 'dm_id': new['dm_id'], 'message': f'Hello @{user_handle}'}
                 )  
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} tagged you in {new['dm_name']}: Hello @{user_handle}"

def test_message_send_later_notification(new):
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # tagging user1 in channel message 
    requests.post(config.url+ 'message/sendlater/v1', 
                  json={'token': new['token2'], 'channel_id': new['channel_id'], 'message': f'Hello @{user_handle}', 
                        'time_sent': int(time.time()) + 0}
                 )
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} tagged you in channel: Hello @{user_handle}"
      
def test_dm_send_later_notification(new):
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # tagging user1 in dm message 
    requests.post(config.url+ 'message/sendlaterdm/v1', 
                  json={'token': new['token2'], 'dm_id': new['dm_id'], 'message': f'Hello @{user_handle}', 
                        'time_sent': int(time.time()) + 0}
                 )  
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} tagged you in {new['dm_name']}: Hello @{user_handle}"
    
    
'''
def test_react_channel_notification(new):
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # user1 sends channel message 
    message_send_return = requests.post(config.url+ 'message/send/v1', 
                  json={'token': new['token1'], 'channel_id': new['channel_id'], 'message': f'Hello everyone'}
                 )
    message_id_data = message_send_return.json()
    message_id = message_id_data['message_id']
    
    # user2 reacts to channel message 
    requests.post(config.url + 'message/react/v1', json = {'token': new['token2'], 'message_id': message_id, 'react_id': 1})   
                             
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} reacted to your message in channel"
    
def test_react_dm_notification(new):
    user_handle = new['user_handle1']
    user_handle2 = new['user_handle2']
    # user1 sends dm  
    message_send_return = requests.post(config.url+ 'message/senddm/v1', 
                  json={'token': new['token1'], 'dm_id': new['dm_id'], 'message': f'Hello everyone'}
                 )
    message_id_data = message_send_return.json()
    message_id = message_id_data['message_id']
    
    # user2 reacts to channel message 
    requests.post(config.url + 'message/react/v1', json = {'token': new['token2'], 'message_id': message_id, 'react_id': 1})   
                             
    message = requests.get(config.url + 'notifications/get/v1', params= {'token': new['token1']})
    message = message.json()
    assert message['notifications'][0]['notification_message'] == f"{user_handle2} reacted to your message in {new['dm_name']}"
    
'''
