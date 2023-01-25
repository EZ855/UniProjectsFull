import pytest
from src.channels import channels_list_v1
from src.error import AccessError
from src.other import clear_v1
from src.auth import auth_login_v1, auth_register_v1
from src.channels import channels_create_v1
#cant access data_store
@pytest.fixture
def setup():
    clear_v1()
    auth_register_v1("oogabooga@gmail.com", "Caveman123","Ungus", "Bungus")
    
def test_channels_none(setup): #tests for if the given user id no channels exist
    empty_list = {'channels': []}
    lists = channels_list_v1(1)
    assert(lists == empty_list)
    
def test_one_channel_public(setup):
    channels_create_v1(1, "Random channel", True)
    assert {'channel_id': 1, 'name': 'Random channel'} in channels_list_v1(1)['channels']
    
def test_multiple_channels_public(setup):
    channels_create_v1(1, "Random channel", True)
    channels_create_v1(1, "Second channel", True)
    assert [{'channel_id': 1, 'name': 'Random channel'}, {'channel_id': 2, 'name': 'Second channel'}] == channels_list_v1(1)['channels']
    
def test_only_user_channels_listed(setup):
    auth_register_v1("monty@python.com", "password", "monty", "python")['auth_user_id']
    channels_create_v1(1, "Random channel", True)
    channels_create_v1(1, "Second channel", True)
    channels_create_v1(2, "third channel", True)
    assert [{'channel_id': 1, 'name': 'Random channel'}, {'channel_id': 2, 'name': 'Second channel'}] == channels_list_v1(1)['channels']

