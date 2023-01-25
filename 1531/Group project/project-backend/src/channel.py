from src.data_store import data_store
from src.error import InputError, AccessError
from src.helpers import decode_jwt
from src.notifications import notification_channel_invite

def channel_invite_v1(auth_user_id, channel_id, u_id):
    store = data_store.get()
    cha_id_list=[]
    for chas in store['channels']:
        cha_id_list.append(chas['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError(description="Invalid channel id")
    u_id_list2=[]
    for uid in store['users']:
        u_id_list2.append(uid['u_id'])
    if u_id not in u_id_list2:
        raise InputError(description="Invalid user id")
    u_id_allmember_list=[]
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    for take_user in store['users']:
        if take_user['u_id'] == u_id:
            new_dic = take_user
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            if auth_user_id not in u_id_allmember_list:
                raise AccessError(description="Authorised user is not a member of the channel")
            elif u_id in u_id_allmember_list:
                raise InputError(description="Already a member of channel")
            else:
                channel.setdefault('all_members',[]).append(new_dic)
                notification_channel_invite(auth_user_id, u_id, channel_id)
				
    data_store.set(store)   
    return {
    }

def channel_details_v1(auth_user_id, channel_id):
    
    store = data_store.get()
    channels = store['channels']

    for channel in channels:
        if channel['channel_id'] == channel_id:
            """
            Iterates through the owner_members and all_members and if the auth_user_id is found,
            returns the channel's name, public status, all owner members and all regular members.
            """            
            for chan in (channel['owner_members'], channel['all_members']):
                for user in chan:
                    if user['u_id'] == auth_user_id:
                        return {
                                'name': channel['name'],
                                'is_public': channel['is_public'],
                                'owner_members': channel['owner_members'],
                                'all_members': channel['all_members'],
                        }

            """
            If the auth_user_id does not match with any of the users in either channel, an AccessError is raised.
            """
            raise AccessError(description="Channel_id is valid and the authorised user is not a member of the channel")
            
    """
    If the channel_id does not match with any of the channels, an InputError is raised.
    """        
    raise InputError(description="Channel_id does not refer to a valid channel")
    




def channel_messages_v1(auth_user_id, channel_id, start):
    '''
    Given the user's id, desired channel and starting index and returns the at most
    50 messages, the start index and the end index
    
    Arguments:
        auth_user_id (int) -- the id of the user requesting messages
        channel_id (int) -- the id of the channel the user is requesting mesages from
        start (int) -- the index of the first message that will be returned to the users
        
    Exceptions:
       InputError -- No channel exists with corresponding channel_id
        InputError -- Start index is larger than messages list
       AccessError -- The user requesting messages does not have access to the channel_details_v1
    
    Return value:
        Returns a list of a most 50 messages, the starting index and 
        the ending index (which is -1 if there are less than 50 messages returned)
    '''
    store = data_store.get()
    channels = store['channels']
    
    curr_channel = {}
    valid_channel_id = False
    valid_access = False
    
    
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
        
    
    # Checks if starting index is outside expected bounds
    if len(curr_channel['messages']) < start or start < 0:
        raise InputError(description="Starting index exceeds bounds of message list")
    
    num_msg_loaded = 0
    messages = []
    end = start + 50
    
    for x in range(len(curr_channel['messages'])):
        if x >= start:
            num_msg_loaded = num_msg_loaded + 1
            if auth_user_id in curr_channel['messages'][x]['reacts'][0]['u_ids']:
                curr_channel['messages'][x]['reacts'][0]['is_this_user_reacted'] = True
            else:
                curr_channel['messages'][x]['reacts'][0]['is_this_user_reacted'] = False
            messages.append(curr_channel['messages'][x])
            
            if num_msg_loaded == 50:
                break
    
    if num_msg_loaded != 50:
        end = -1
        
    return {
        "messages": messages,
        "start": start,
        "end": end,
    }

def channel_join_v1(auth_user_id, channel_id):
    store = data_store.get()
    u_id_allmember_list=[]
    cha_id_list=[]
    
    for chas in store['channels']:
        cha_id_list.append(chas['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError(description="Invalid channel id")
    for take_user in store['users']:
        if take_user['u_id']== auth_user_id:
            new_dic = take_user
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    for channel in store['channels']:
        if channel_id == channel['channel_id']: 
            if auth_user_id not in u_id_allmember_list:
                if channel['is_public'] == False and auth_user_id not in store['global_owners']:
                    raise AccessError("You can not join")
                else:
                    channel.setdefault('all_members',[]).append(new_dic)
            else:
                raise InputError(description="Already a member of channel")
    data_store.set(store)
    return {
    }
def channel_leave_v1(auth_user_id, channel_id):
    store = data_store.get()
    u_id_allmember_list=[]
    cha_id_list=[] 
    for chas in store['channels']:
        cha_id_list.append(chas['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError("Invalid channel id")
    #InputError
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    if channel_id in cha_id_list and auth_user_id not in u_id_allmember_list:
        raise AccessError("You are not a member of this channel")
    #AccessError
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for all_mem in channel['all_members']:
                if all_mem['u_id']==auth_user_id:
                    channel['all_members'].remove(all_mem)
            for all_own in channel['owner_members']:
                if all_own['u_id']==auth_user_id:
                    channel['owner_members'].remove(all_own)    
    #remove the auth from channel
    data_store.set(store)
    return {
    }
    
    
def channel_removeowner_v1(auth_user_id, channel_id, u_id):  
    store = data_store.get()
    u_id_allmember_list=[]
    u_id_ownermember_list=[]
    cha_id_list=[]
    u_id_list2=[]
    permission = False
    for chas in store['channels']:
        cha_id_list.append(chas['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError("Invalid channel id")
    #InputError channel_id does not refer to a valid channel
    for uid in store['users']:
        u_id_list2.append(uid['u_id'])
    if u_id not in u_id_list2:
        raise InputError("Invalid user id")
    #InputError u_id does not refer to a valid user
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['owner_members']:
                u_id_ownermember_list.append(member['u_id'])
    if u_id in u_id_list2 and u_id not in u_id_ownermember_list:
        raise InputError("That user is not a owner of this channel.")
    #InputError u_id refers to a user who is not a owner of the channel
    if u_id in u_id_list2 and u_id in u_id_ownermember_list and len(u_id_ownermember_list)==1:
        raise InputError("That user is only owner of this channel.")              
    #InputError u_id refers to a user who is only an owner of the channel
    if auth_user_id in u_id_ownermember_list:
        permission = True
    if auth_user_id in u_id_allmember_list and auth_user_id in store['global_owners']:
        permission = True
    if not permission:
        raise AccessError("You do not have the permission.")
    #AccessError channel_id is valid and the authorised user does not have owner permissions in the channel
    for chann in store['channels']:
        if channel_id == chann['channel_id']:
            for all_m in chann['owner_members']:
                if all_m['u_id']==u_id:
                    chann['owner_members'].remove(all_m)
     #removeowner
    data_store.set(store)
    return {
    }
    
    
def channel_addowner_v1(auth_user_id, channel_id, u_id):  
    store = data_store.get()
    u_id_allmember_list=[]
    u_id_ownermember_list=[]
    cha_id_list=[]
    u_id_list2=[]
    permission = False
    for chas in store['channels']:
        cha_id_list.append(chas['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError("Invalid channel id")
    #InputError channel_id does not refer to a valid channel
    for uid in store['users']:
        u_id_list2.append(uid['u_id'])
    if u_id not in u_id_list2:
        raise InputError("Invalid user id")
    #InputError u_id does not refer to a valid user
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    if u_id in u_id_list2 and u_id not in u_id_allmember_list:
        raise InputError("That user is not a member of this channel.")
    #InputError u_id refers to a user who is not a member of the channel
    for chan in store['channels']:
        if channel_id == chan['channel_id']:
            for own_mem in chan['owner_members']:
                u_id_ownermember_list.append(own_mem['u_id'])
    if u_id in u_id_ownermember_list:
        raise InputError("That user is already a owner of this channel.")
    #InputError u_id refers to a user who is already an owner of the channel
    if auth_user_id in u_id_ownermember_list:
        permission = True
    if auth_user_id in u_id_allmember_list and auth_user_id in store['global_owners']:
        permission = True
    if not permission:
        raise AccessError("You do not have the permission.")
    #AccessError channel_id is valid and the authorised user does not have owner permissions in the channel
    for chann in store['channels']:
        if channel_id == chann['channel_id']:
            for all_m in chann['all_members']:
                if all_m['u_id']==u_id:
                    chann['owner_members'].append(all_m)
     #addowner
    data_store.set(store)
    return {
    }
