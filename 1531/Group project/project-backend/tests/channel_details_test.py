import pytest

from src.auth import auth_register_v1
from src.channels import channels_create_v1
from src.channel import channel_details_v1
from src.error import InputError
from src.error import AccessError
from src.other import clear_v1
    
def test_channel_details_invalid_channel_id_no_channels():
    clear_v1()
    user_id = auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    channel_id = channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    
    clear_v1()
    auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        channel_details_v1(user_id['auth_user_id'], channel_id['channel_id'])

def test_channel_details_invalid_channel_id_populated_list():
    clear_v1()
    user_id = auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    channel_id_2 = channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    
    clear_v1()
    auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    with pytest.raises(InputError):
        channel_details_v1(user_id['auth_user_id'], channel_id_2['channel_id'])

def test_channel_details_unauthorised_user():
    clear_v1()
    user_id = auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    second_user_id = auth_register_v1('monty2@mail.valid', 'password', 'monty', 'python')
    channel_id = channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    with pytest.raises(AccessError):
        channel_details_v1(second_user_id['auth_user_id'], channel_id['channel_id'])
        
def test_channel_details_unregistered_user():
    clear_v1()
    user_id = auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    channel_id = channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    with pytest.raises(AccessError):
        channel_details_v1(user_id['auth_user_id'] + 1, channel_id['channel_id'])

def test_channel_details_functionality():
    clear_v1()
    user_id = auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    channel_id = channels_create_v1(user_id['auth_user_id'], 'montychannel', True)
    assert(len(channel_details_v1(user_id['auth_user_id'], channel_id['channel_id'])) == 4)
