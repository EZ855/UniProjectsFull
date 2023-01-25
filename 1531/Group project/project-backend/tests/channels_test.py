import pytest

from src.channels import channels_create_v1, channels_listall_v1
from src.error import InputError, AccessError
from src.other import clear_v1
from src.auth import auth_register_v1


@pytest.fixture
def clear():
    clear_v1()
    

def test_channels_create_short_name(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")
    with pytest.raises(InputError):
        assert channels_create_v1(user_id_1,"", True)
        
def test_channels_create_long_name(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")
    with pytest.raises(InputError):
        assert channels_create_v1(user_id_1,"123456789012345678901", True)

def test_channels_create_invalid_user_id(clear):
    auth_register_v1("monty@python.com", "montypass", "monty", "python")
    with pytest.raises(AccessError):
        channels_create_v1(-1, "channel", True)       

def test_channels_create_first_channel_id(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")["auth_user_id"]
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", True)['channel_id'], int))

def test_channels_create_two_channels_id(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")["auth_user_id"]
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", True)['channel_id'], int))
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", True)['channel_id'], int))

def test_channels_create_three_channels_id(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")["auth_user_id"]
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", True)['channel_id'], int))
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", True)['channel_id'], int))
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", True)['channel_id'], int))
    
def test_channels_create_first_channel_id_boolean_false(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")["auth_user_id"]
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", False)['channel_id'], int))

def test_channels_create_second_channel_id_boolean_false(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")["auth_user_id"]
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", False)['channel_id'], int))
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", False)['channel_id'], int))

def test_channels_create_three_channels_boolean_false(clear):
    user_id_1 = auth_register_v1("123@123.com", "password", "Bob", "Robert")["auth_user_id"]
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", False)['channel_id'], int))
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", False)['channel_id'], int))
    assert(isinstance(channels_create_v1(user_id_1,"Channel Name", False)['channel_id'], int))
