from src.data_store import data_store
from src.error import InputError
from src.error import AccessError
from datetime import timezone
import datetime
from src.tag import tag_user
from src. notifications import notification_react
import threading
import time

def message_send_v1(auth_user_id, channel_id, message):
    """
    Given an auth_user_id, channel_id, and message, sends a message from the authorised 
    user to the channel specified by channel_id and returns the message_id
    
    Arguments:
        auth_user_id (integer) -- user who is requesting to send a message to the channel
        channel_id (integer) -- id of the channel
        message (string) -- message being sent

    Exceptions:
        InputError -- Occurs when channel_id does not refer to a valid channel
        InputError -- Occurs when length of message is less than 1 or over 1000 characters
        AccessError -- Occurs when channel_id is valid and the authorised user is not a member of the channel

    Return value:
        Dictionary containing the message_id
        
    """
    store = data_store.get()
    channels = store['channels']
    curr_channel = {}
    valid_channel_id = False
    valid_access = False
    
    
    if len(message) < 1 or len(message) > 1000:
        raise InputError(description="message length too long or short")
    
    for channel in channels:
        if channel['channel_id'] == channel_id:
            valid_channel_id = True
            curr_channel = channel
            for member in channel['all_members']:
                if member['u_id'] == auth_user_id:
                     valid_access = True
                     
     # Checks if the channel_id is one that already exists
    if not valid_channel_id:
        raise InputError(description="invalid channel id")
        
    # Checks if user has access to channel
    if not valid_access:
        raise AccessError(description="User does not have access to channel")
    store['message_id_curr'] = store['message_id_curr'] + 1 
    message_dict= {
            'message_id': store['message_id_curr'],
            'u_id': auth_user_id,
            'is_pinned': False,
            'reacts': [{"is_this_user_reacted":False,"react_id":1,"u_ids":[]}],
            'message': message,
            'time_created': int(time.time())
    }    
    curr_channel['messages'].insert(0, message_dict)
    
    # check for user tags
    tag_user(auth_user_id, message, store['message_id_curr'], is_channel=True, ch_id=channel_id)
    
    data_store.set(store)
    return {'message_id': message_dict['message_id']}


def message_edit_v1(auth_user_id, message_id, message):
    """
    Given an auth_user_id, message_id, and message, it updates the text located at
    message_id with the new text message, if message is an empty string it deletes it
    
    Arguments:
        auth_user_id (integer) -- user who is requesting to edit a message 
        message_id (integer) -- id of the message
        message (string) -- new text in message

    Exceptions:
        InputError -- Occurs when the length of message is over 1000 characters
        InputError -- Occurs when message_id does not refer to a valid message
                      within a channel/DM that the authorised user joined
                      
        AccessError -- Occurs when authorised user did not create the original message
                       or does not have permission within the channel/DM the message 
                       was located in

    Return value:
        N/A
        
    """
    store = data_store.get()
    channels = store['channels']
    dms = store['dms']
    valid_msg_id = False
    access = False
    in_dm = False
    is_member = False
    msg_location = {} # channel or dm that contains the message
    
    if message_id > store['message_id_curr']:
        raise InputError(description= "Invalid message id")
    
    
    if len(message) > 1000:
        raise InputError(description="Message too long")
        
    
        
    for channel in channels:
        for x in range(len(channel['messages'])):
            if channel['messages'][x]['message_id'] == message_id:
                msg_location = channel
                msg_owner_id = channel['messages'][x]['u_id']
                message_index = x
                valid_msg_id = True
    
    for dm in dms:
        for x in range(len(dm['messages'])):
            if dm['messages'][x]['message_id'] == message_id:
                msg_location = dm
                msg_owner_id = dm['messages'][x]['u_id']
                message_index = x
                valid_msg_id = True
                in_dm = True
                
    if not valid_msg_id:
        raise InputError(description="Message not found")
    
    if not in_dm:
        for member in msg_location['all_members']:
            if member['u_id'] == auth_user_id:
                is_member = True
    
    if is_member:
        if auth_user_id in store['global_owners']:
                access = True
    
    for member in msg_location['owner_members']:
        if member['u_id'] == auth_user_id:
            access = True
    
    if auth_user_id == msg_owner_id:
        access = True
        
    if not access:
        raise AccessError(description="Invalid access to message id")
            
    if access and valid_msg_id:
        msg_location['messages'][message_index]['message'] = message
        if not message:
            del msg_location['messages'][message_index]
    
    # check for user tags
    tag_user(auth_user_id, message, message_id, is_channel=True, ch_id=channel['channel_id'])   
    
    data_store.set(store)
    
    return{}


def message_remove_v1(auth_user_id, message_id):
    """
    Given an auth_user_id, message_id, removes message with id message_id
    
    Arguments:
        auth_user_id (integer) -- user who is requesting to edit a message 
        message_id (integer) -- id of the message

    Exceptions:
        InputError -- Occurs when message_id does not refer to a valid message 
                      within a channel/DM that the authorised user has joined
                      
        AccessError -- Occurs when authorised user did not create the original message
                       or does not have permission within the channel/DM the message 
                       was located in

    Return value:
        N/A
    """
        
    store = data_store.get()
    channels = store['channels']
    dms = store['dms']
    valid_msg_id = False
    access = False
    in_dm = False
    is_member = False
    msg_location = {} # channel or dm that contains the message
    
    if message_id < 1 or message_id > store['message_id_curr']:
        raise InputError(description="Invalid message id")
        
    
        
    for channel in channels:
        for x in range(len(channel['messages'])):
            if channel['messages'][x]['message_id'] == message_id:
                msg_location = channel
                msg_owner_id = channel['messages'][x]['u_id']
                message_index = x
                valid_msg_id = True
    
    for dm in dms:
        for x in range(len(dm['messages'])):
            if dm['messages'][x]['message_id'] == message_id:
                msg_location = dm
                msg_owner_id = dm['messages'][x]['u_id']
                message_index = x
                valid_msg_id = True
                in_dm = True
                
    if not valid_msg_id:
        raise InputError(description="Message not found")
    
    if not in_dm:
        for member in msg_location['all_members']:
            if member['u_id'] == auth_user_id:
                is_member = True
    
    if is_member:
        if auth_user_id in store['global_owners']:
                access = True

    for member in msg_location['owner_members']:
        if member['u_id'] == auth_user_id:
            access = True
    
    if auth_user_id == msg_owner_id:
        access = True
        
    if not access:
        raise AccessError(description="Invalid access to message id")
            
    if access and valid_msg_id:
        del msg_location['messages'][message_index]
    
    data_store.set(store)
    
    return{}
    
    
def message_senddm_v1(auth_user_id, dm_id, message):
    """
    Given an auth_user_id, dm_id, and message, sends a message from the authorised 
    user to the channel specified by dm_id and returns the message_id
    
    Arguments:
        auth_user_id (integer) -- user who is requesting to send a message to the dm
        dm_id (integer) -- id of the dm
        message (string) -- message being sent

    Exceptions:
        InputError -- Occurs when dm_id does not refer to a valid dm
        InputError -- Occurs when length of message is less than 1 or over 1000 characters
        AccessError -- Occurs when dm_id is valid and the authorised user is not a member of the dm

    Return value:
        Dictionary containing the message_id
        
    """
    store = data_store.get()
    dms = store['dms']
    curr_dm = {}
    valid_dm_id = False
    valid_access = False
    
    
    if len(message) < 1 or len(message) > 1000:
        raise InputError(description="message length too long or short")
    
    for dm in dms:
        if dm['dm_id'] == dm_id:
            valid_dm_id = True
            curr_dm = dm
            for member in dm['all_members']:
                if member['u_id'] == auth_user_id:
                     valid_access = True
                     
     # Checks if the channel_id is one that already exists
    if not valid_dm_id:
        raise InputError(description="invalid dm id")
        
    # Checks if user has access to channel
    if not valid_access:
        raise AccessError(description="User does not have access to dm")
    store['message_id_curr'] = store['message_id_curr'] + 1 
    message_dict= {
            'message_id': store['message_id_curr'],
            'u_id': auth_user_id,
            'is_pinned': False,
            'reacts': [{"is_this_user_reacted":False,"react_id":1,"u_ids":[]}],
            'message': message,
            'time_created': int(time.time())
    }    
    curr_dm['messages'].insert(0, message_dict)
    
    # check for user tags
    tag_user(auth_user_id, message, store['message_id_curr'], is_dm=True, dm_id=dm_id)
    
    data_store.set(store)
    return {'message_id': message_dict['message_id']}


def check_errors(valid_ch_dm_id, valid_access, valid_og_msg_id):
    if not valid_og_msg_id:
        raise InputError(description="Message not found")
        
    if not valid_ch_dm_id:
        raise InputError(description="invalid channel or dm  id")
        
    # Checks if user has access to channel
    if not valid_access:
        raise AccessError(description="User does not have access to channel")
        
    pass    
    
def message_share_v1(auth_user_id, og_message_id, message, channel_id, dm_id):
    store = data_store.get()
    valid_ch_dm_id = False
    valid_access = False
    valid_og_msg_id = False
    
    if  len(message) > 1000:
        raise InputError(description="message length too long")

    
    if channel_id == -1 and dm_id != -1:
        
        dms = store['dms']
        for dm in dms:
            if dm['dm_id'] == dm_id:
                valid_ch_dm_id = True
                for member in dm['all_members']:
                    if member['u_id'] == auth_user_id:
                         valid_access = True
                         
        for dm in dms:
            for x in range(len(dm['messages'])):
                if dm['messages'][x]['message_id'] == og_message_id:
                    valid_og_msg_id = True
                    og_message = dm['messages'][x]['message']

        check_errors(valid_ch_dm_id, valid_access, valid_og_msg_id)
        
        
        #assuming that the shared string must also be under 1000
        shared_message = f"\" \n{og_message}\n\" \n{message}"
        
        shared_message_id = message_senddm_v1(auth_user_id, dm_id, shared_message)
        
        return {"shared_message_id":shared_message_id}    
            
    elif dm_id == -1 and channel_id != -1:
        
        channels = store['channels']
        
        for channel in channels:
            if channel['channel_id'] == channel_id:
                valid_ch_dm_id = True
                for member in channel['all_members']:
                    if member['u_id'] == auth_user_id:
                         valid_access = True
                     
                     
        for channel in channels:
            for x in range(len(channel['messages'])):
                if channel['messages'][x]['message_id'] == og_message_id:
                    valid_og_msg_id = True
                    og_message = channel['messages'][x]['message']
        
        check_errors(valid_ch_dm_id, valid_access, valid_og_msg_id)
        
        #assuming that the shared string must also be under 1000
        shared_message = f"\" \n{og_message}\n\" \n{message}"
        
        shared_message_id = message_send_v1(auth_user_id, channel_id, shared_message)
        
        return {"shared_message_id":shared_message_id}
    else:
        raise InputError(description="Both or neither of the channel/dm ids are -1")
    
def msg_send_thread(auth_user_id, channel_id, message, message_id):
    
    store = data_store.get()
    channels = store['channels']
    curr_channel = {}
    
    for channel in channels:
        if channel['channel_id'] == channel_id:
            curr_channel = channel
    
    message_dict= {
            'message_id': store['message_id_curr'],
            'u_id': auth_user_id,
            'is_pinned': False,
            'reacts': [{"is_this_user_reacted":False,"react_id":1,"u_ids":[]}],
            'message': message,
            'time_created': int(time.time())
    }    
    curr_channel['messages'].insert(0, message_dict)
    
    # check for user tags
    tag_user(auth_user_id, message, store['message_id_curr'], is_channel=True, ch_id=channel_id)
    
    data_store.set(store)
    
    
def message_sendlater_v1(auth_user_id, channel_id, message, time_sent):
    
    store = data_store.get()
    channels = store['channels']
    valid_channel_id = False
    valid_access = False
    time_wait = 0

    if len(message) < 1 or len(message) > 1000:
        raise InputError(description="message length too long or short")
    
    for channel in channels:
        if channel['channel_id'] == channel_id:
            valid_channel_id = True
            for member in channel['all_members']:
                if member['u_id'] == auth_user_id:
                     valid_access = True
                     
     # Checks if the channel_id is one that already exists
    if not valid_channel_id:
        raise InputError(description="invalid channel id")
        
    # Checks if user has access to channel
    if not valid_access:
        raise AccessError(description="User does not have access to channel")
        
    store['message_id_curr'] = store['message_id_curr'] + 1 
    message_id = store['message_id_curr']
    data_store.set(store)
    

    if time_sent < int(time.time()):
        raise InputError(description="time is in past")
    
    time_wait = time_sent - int(time.time())

    t = threading.Timer(time_wait, msg_send_thread, [auth_user_id, channel_id, message, message_id])
    t.start()

    return {"msg_id":message_id}

def msg_senddm_thread(auth_user_id, dm_id, message, message_id):
    store = data_store.get()
    dms = store['dms']
    curr_dm = {}
    
    for dm in dms:
        if dm['dm_id'] == dm_id:
            curr_dm = dm 
    
    message_dict= {
            'message_id': store['message_id_curr'],
            'u_id': auth_user_id,
            'is_pinned': False,
            'reacts': [{"is_this_user_reacted":False,"react_id":1,"u_ids":[]}],
            'message': message,
            'time_created': int(time.time())
    }    
    curr_dm['messages'].insert(0, message_dict)
    
    # check for user tags
    tag_user(auth_user_id, message, store['message_id_curr'], is_dm=True, dm_id=dm_id)
    
    data_store.set(store)

def message_sendlaterdm_v1(auth_user_id, dm_id, message, time_sent):   
    store = data_store.get()
    dms = store['dms']
    valid_dm_id = False
    valid_access = False
    
    
    if len(message) < 1 or len(message) > 1000:
        raise InputError(description="message length too long or short")
    
    for dm in dms:
        if dm['dm_id'] == dm_id:
            valid_dm_id = True
            for member in dm['all_members']:
                if member['u_id'] == auth_user_id:
                     valid_access = True
                     
     # Checks if the channel_id is one that already exists
    if not valid_dm_id:
        raise InputError(description="invalid dm id")
        
    # Checks if user has access to channel
    if not valid_access:
        raise AccessError(description="User does not have access to dm")
        
    store['message_id_curr'] = store['message_id_curr'] + 1 
    message_id = store['message_id_curr']
    data_store.set(store)
    

    if time_sent < int(time.time()):
        raise InputError(description="Time is in past")
    
    time_wait = time_sent - int(time.time())

    t = threading.Timer(time_wait, msg_senddm_thread, [auth_user_id, dm_id, message, message_id])
    t.start()

    return {"msg_id":message_id}
    
def message_react_v1(auth_user_id, message_id, react_id):
    
    store = data_store.get()
    channels = store['channels']
    dms = store['dms']
    msg_location = {}
    is_member = False
   
    channel_id_notification = -1
    dm_id_notification = -1
    
    if react_id != 1:
        raise InputError(description="Not a valid react")
    
    for channel in channels:
        for x in range(len(channel['messages'])):
            if channel['messages'][x]['message_id'] == message_id:
                msg_location = channel
                message_index = x
                channel_id_notification = message_id
    
    for dm in dms:
        for x in range(len(dm['messages'])):
            if dm['messages'][x]['message_id'] == message_id:
                msg_location = dm
                message_index = x 
                dm_id_notification = message_id
                
    if auth_user_id in msg_location['messages'][message_index]['reacts'][0]['u_ids']:
        raise InputError(description="Already reacted")
        
    for member in msg_location['all_members']:
        if member['u_id'] == auth_user_id:
            is_member = True
            
    if not is_member:        
        raise InputError(description="The message is not in a channel the user has joined")
        
        
    msg_location['messages'][message_index]['reacts'][0]['u_ids'].append(auth_user_id)
    
    # creating notfication   
    message_owner_id = msg_location['messages'][message_index]['u_id']
    notification_react(
        auth_user_id, message_owner_id, msg_location['name'], 
        channel_id_notification, dm_id_notification
        )
       
    data_store.set(store)
    return {}
    
def message_unreact_v1(auth_user_id, message_id, react_id):     
  
    store = data_store.get()
    channels = store['channels']
    dms = store['dms']
    msg_location = {}
    is_member = False
    
    if react_id != 1:
        raise InputError(description="Not a valid react")
    
    for channel in channels:
        for x in range(len(channel['messages'])):
            if channel['messages'][x]['message_id'] == message_id:
                msg_location = channel
                message_index = x
                
    
    for dm in dms:
        for x in range(len(dm['messages'])):
            if dm['messages'][x]['message_id'] == message_id:
                msg_location = dm
                message_index = x
                
    
        
    for member in msg_location['all_members']:
        if member['u_id'] == auth_user_id:
            is_member = True
            
    if not is_member:        
        raise InputError(description="The message is not in a channel the user has joined")
        
    if auth_user_id not in msg_location['messages'][message_index]['reacts'][0]['u_ids']:
        raise InputError(description="The message does not contain a react with ID react_id from the authorised user") 
        
    msg_location['messages'][message_index]['reacts'][0]['u_ids'].remove(auth_user_id)
    data_store.set(store)
    return {}
    
def message_pin_v1(auth_user_id, message_id):
    
    store = data_store.get()
    channels = store['channels']
    dms = store['dms']
    msg_location = {}
    is_member = False
    has_perms = False
    
    
    for channel in channels:
        for x in range(len(channel['messages'])):
            if channel['messages'][x]['message_id'] == message_id:
                msg_location = channel
                message_index = x
                
    
    for dm in dms:
        for x in range(len(dm['messages'])):
            if dm['messages'][x]['message_id'] == message_id:
                msg_location = dm
                message_index = x
                
    for member in msg_location['all_members']:
        if member['u_id'] == auth_user_id:
            is_member = True
            
    if not is_member:        
        raise InputError(description="The message is not in a channel the user has joined")
    
    if msg_location['messages'][message_index]['is_pinned'] == True:
        raise InputError(description="The message is already pinned")
    
    if is_member:
        for member in msg_location['owner_members']:
            if auth_user_id == member['u_id']:
                has_perms = True
        
        if auth_user_id in store['global_owners']:
            has_perms = True
    
    if not has_perms:
        raise AccessError(description="You do not have owner permission")
    
    
    msg_location['messages'][message_index]['is_pinned'] = True    
    data_store.set(store)
    return {}
    
def message_unpin_v1(auth_user_id, message_id):

    store = data_store.get()
    channels = store['channels']
    dms = store['dms']
    msg_location = {}
    is_member = False
    has_perms = False
    
    
    for channel in channels:
        for x in range(len(channel['messages'])):
            if channel['messages'][x]['message_id'] == message_id:
                msg_location = channel
                message_index = x
                
    
    for dm in dms:
        for x in range(len(dm['messages'])):
            if dm['messages'][x]['message_id'] == message_id:
                msg_location = dm
                message_index = x
                
    for member in msg_location['all_members']:
        if member['u_id'] == auth_user_id:
            is_member = True
            
    if not is_member:        
        raise InputError(description="The message is not in a channel the user has joined")
    
    if msg_location['messages'][message_index]['is_pinned'] == False:
        raise InputError(description="The message has not been pinned")
    
    if is_member:
        for member in msg_location['owner_members']:
            if auth_user_id == member['u_id']:
                has_perms = True
        
        if auth_user_id in store['global_owners']:
            has_perms = True
    
    if not has_perms:
        raise AccessError(description="You do not have owner permission")
    
    
    msg_location['messages'][message_index]['is_pinned'] = False    
    data_store.set(store)
    return {}
    
    
