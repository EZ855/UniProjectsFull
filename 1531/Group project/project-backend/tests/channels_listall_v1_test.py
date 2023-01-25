import pytest
from src.channels import channels_listall_v1
from src.error import InputError, AccessError
from src.other import clear_v1
from src.auth import auth_login_v1, auth_register_v1
from src.channels import channels_create_v1
#cant access data_store
@pytest.fixture
def setup():
    clear_v1()
    return auth_register_v1("oogabooga@gmail.com", "Caveman123","Ungus", "Bungus")['auth_user_id']
    
def test_channels_null(setup):#tests for if there are no channels
    empty_list = {'channels': []}
    lists = channels_listall_v1(setup)
    assert(lists == empty_list)
    
        
def test_multiple_channels(setup):
    channels_create_v1(1, "monty channel", True)
    channels_create_v1(1, "python channel", True)  
    expected_channels = [{'channel_id': 1, 'name': 'monty channel'}, {'channel_id': 2, 'name': 'python channel'}]
    assert channels_listall_v1(1)['channels'] == expected_channels
