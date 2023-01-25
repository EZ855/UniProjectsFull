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
    
    #registering user2 
    info2 = {'email':'python@mail.valid' , 'password': 'password', 'name_first': 'python', 'name_last': 'monty'}
    resp2 = requests.post(config.url + 'auth/register/v2', json = info2) # registering user 
    resp_data2 = resp2.json()
    u_id2 = resp_data2["auth_user_id"]
    
    # creating channel and dm 
    requests.post(config.url + 'channels/create/v2', json={'name': 'channel', 'token': token, 'is_public': True})
    requests.post(config.url + 'dm/create/v1', json={'token': token, 'u_ids': [u_id2]})
    
    return {"token": token}
    
def test_functionality(new):
    info = {'token': new['token'], 'img_url': "http://i.pinimg.com/736x/c2/ca/fe/c2cafe93c0f6b1ea21341d2810484b57.jpg", 
            'x_start': 0, 'y_start': 0, 'x_end': 150, 'y_end': 150}
    resp = requests.post(config.url + "/user/profile/uploadphoto/v1", json = info)
    assert resp.status_code == OK

def test_dimensions_too_large(new):
    info = {'token': new['token'], 'img_url': "https://i.pinimg.com/736x/c2/ca/fe/c2cafe93c0f6b1ea21341d2810484b57.jpg", 
            'x_start': 0, 'y_start': 0, 'x_end': 100000, 'y_end': 150}
    resp = requests.post(config.url + "/user/profile/uploadphoto/v1", json = info)
    assert resp.status_code == InputError.code

def test_invalid_dimensions(new):
    info = {'token': new['token'], 'img_url': "https://i.pinimg.com/736x/c2/ca/fe/c2cafe93c0f6b1ea21341d2810484b57.jpg", 
            'x_start': 10, 'y_start': 10, 'x_end': 0, 'y_end': 0}
    resp = requests.post(config.url + "/user/profile/uploadphoto/v1", json = info)
    assert resp.status_code == InputError.code
    
def test_invalid_type(new):
    info = {'token': new['token'], 'img_url': 'https://www.pngall.com/wp-content/uploads/2016/04/Plum-PNG-HD.png', 
            'x_start': 0, 'y_start': 0, 'x_end': 100, 'y_end': 100}
    resp = requests.post(config.url + "/user/profile/uploadphoto/v1", json = info)
    assert resp.status_code == InputError.code
    
