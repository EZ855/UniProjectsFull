import pytest
from src.channel import channel_messages_v1
from src.channels import channels_create_v1
from src.error import InputError, AccessError
from src.auth import auth_register_v1
from src.other import clear_v1

def test_invalid_channel():
    clear_v1()
    # To test for InputError when channel_id 1 doesn't exist
    with pytest.raises(InputError):
        channel_messages_v1(1, 1, 1) 
     
    user_id = auth_register_v1("monty@python.com", "montypass", "monty", "python")['auth_user_id']    
    channels_create_v1(user_id, 'tempName', True)        
    # Testing negative id values 
    with pytest.raises(InputError):
        channel_messages_v1(1, -1, 1)
  
def test_invalid_user_access():
    clear_v1()
    with pytest.raises(AccessError):
        channels_create_v1(1, 'tempName', True) 
        channel_messages_v1(2, 1, 1)

def test_invalid_start():
    clear_v1()
    # To test for start index is outside of the message list
    user_id = auth_register_v1("monty@python.com", "montypass", "monty", "python")['auth_user_id'] 
    with pytest.raises(InputError):
        channels_create_v1(user_id, 'tempName', True) 
        channel_messages_v1(1, 1, 1) 

def test_no_messages():
    clear_v1()
    user_id = auth_register_v1("monty@python.com", "montypass", "monty", "python")['auth_user_id']
    channel_id = channels_create_v1(user_id, "monty", True)['channel_id']
    messages = channel_messages_v1(user_id, channel_id, 0)
    assert len(messages['messages']) == 0
    assert messages['start'] == 0
    assert messages['end'] == -1

'''  
Can not be properly tested until messaging functionailty added       

def test_message_functionailty():
    clear_v1()
    channels_create_v1(1, 'tempName', 1) 
    msg = channel_messages_v1(1, 1, 1)
    assert msg['start'] == 1,"start has changed"
    assert msg['end'] == -1,"end value is inncorrect"
'''

