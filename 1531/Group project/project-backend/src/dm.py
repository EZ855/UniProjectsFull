from src.data_store import data_store
from src.error import InputError, AccessError
from src. notifications import notification_dm_create


def check_invalid_user(u_ids, users):
    for u_id in u_ids:
        match = False
        for user in users:
            if u_id == user['u_id']:
                match = True
        if not match:
            raise InputError("Invalid user id detected")

def dm_create_v1(auth_user_id, u_ids):
    """
    Given an auth_user_id, and list of u_ids, creates a dm containing, dm_id, name, owner_members, all_members and messages.
    The name of the DM is an alphabetically-sorted, comma-and-space-separated concatenation of the user handles (excluding the creator).
    
    Arguments:
        auth_user_id (integer) -- user who is creating the DM
        u_ids (list) -- list of user ids DM directed to

    Exceptions:
        AccessError -- Occurs when auth_user_id does not exist
        InputError -- Occurs when any id in u_id does not exist

    Return value:
        {dm_id} (i.e. integer in curly brackets)
    """     
      
    name = []
    all_user_info = []
    owner_user_info = []
    store = data_store.get()
    users = store['users']
    dms = store['dms']
    check_invalid_user(u_ids, users)
    for user in users:
        if user['u_id'] in u_ids:
            name.append(user['handle_str'])
            user_info = {"u_id": user["u_id"], "email": user["email"], "name_first": user["name_first"], "name_last": user["name_last"], 
                        "handle_str": user["handle_str"], "profile_img_url": user["profile_img_url"]}
            all_user_info.append(user_info)
        elif user['u_id'] == auth_user_id:
            name.append(user['handle_str'])
            user_info = {"u_id": user["u_id"], "email": user["email"], "name_first": user["name_first"], "name_last": user["name_last"], 
                        "handle_str": user["handle_str"], "profile_img_url": user["profile_img_url"]}
            owner_user_info.append(user_info)
           
    name.sort()
    name = ', '.join(name)
    new_dm_id = len(dms) + 1
    
    """
    Creates a new DM and stores it in data_store
    """
    dms.append(
        {
            'dm_id': new_dm_id, 
            'name': name,  
            'original_creator': owner_user_info,
            'owner_members': owner_user_info,
            'all_members': owner_user_info + all_user_info,
            'messages':[]
       }
    )
    
    for u_id in u_ids:
        notification_dm_create(auth_user_id, u_id, new_dm_id)
               
    return new_dm_id


def dm_list_v1(auth_user_id):
    """
    Given an auth_user_id, returns the list of DMs that the user is a member of.
    
    Arguments:
        auth_user_id (integer) -- user who is requesting the dm info

    Exceptions:
        AccessError -- Occurs when auth_user_id does not exist

    Return value:
        Dictionary with one key ('dms') containing a list of dictionaries with keys
        'dm_id' and 'name' (of the dm).
        
    """
    store = data_store.get()

    all_dms = store['dms']
    user_dms = []
    for dm in all_dms:
        for member in dm['all_members']:
            if member['u_id'] == auth_user_id:
                info = {'dm_id': dm['dm_id'], 'name': dm['name']}
                user_dms.append(info)
                break
    return {
        'dms': user_dms
    }
    
    
def dm_remove_v1(auth_user_id, dm_id):
    """
    Given an auth_user_id, and dm_id, if the user is the creator, deletes the entire DM.
    
    Arguments:
        auth_user_id (integer) -- user who is requesting the DM deletion
        dm_id (integer) -- id of the DM

    Exceptions:
        InputError -- Occurs when the dm_id does not refer to a valid dm
        AccessError -- Occurs when auth_user_id is not the original DM creator
        AccessError -- Occurs when auth_user_id does not exist

    Return value:
        Empty dictionary
        
    """
    store = data_store.get()

    all_dms = store['dms']
    dm_match = False 
    user_match = False
    for dm in range(0, len(all_dms)):
        if dm_id == all_dms[dm]['dm_id']:
            dm_match = True
            if all_dms[dm]['original_creator'][0]['u_id'] == auth_user_id:
                del all_dms[dm]
                user_match = True
                break       
    if not dm_match:
        raise InputError(description="Invalid dm_id")               
    elif not user_match:
        raise AccessError(description="User is not original DM creator")        
    return {'dm_match': dm_match}


def dm_details_v1(auth_user_id, dm_id):
    """
    Given an auth_user_id, and dm_id, if the user is a member of the DM corresponding to the dm_id,
    returns the DM's name and members.
    
    Arguments:
        auth_user_id (integer) -- user who is requesting the DM details
        dm_id (integer) -- id of the DM

    Exceptions:
        InputError -- Occurs when the dm_id does not refer to a valid dm
        AccessError -- Occurs when auth_user_id is not a member
        AccessError -- Occurs when auth_user_id does not exist

    Return value:
        Dictionary containing the name and members of the corresponding dm_id
        
    """
    store = data_store.get()

    all_dms = store['dms']
    info = {}
    dm_match = False
    user_match = False
    for dm in all_dms:
        if dm_id == dm['dm_id']:
            dm_match = True
            for member in dm['all_members']:
                if member['u_id'] == auth_user_id:
                    info['name'] = dm['name']
                    info['members'] = dm['all_members']
                    user_match = True     
                    break
    if not dm_match:
        raise InputError("Invalid dm_id")
    elif not user_match:
        raise AccessError("User not a member of given DM")
        
            
    return info


def dm_leave_v1(auth_user_id, dm_id):
    """
    Given an auth_user_id, and dm_id, if the user is a member of the DM corresponding to the dm_id,
    removes them from all_members or, if they are the creator, from all_members and owner_members.
    
    Arguments:
        auth_user_id (integer) -- user who is requesting to leave the DM
        dm_id (integer) -- id of the DM

    Exceptions:
        InputError -- Occurs when the dm_id does not refer to a valid dm
        AccessError -- Occurs when auth_user_id is not a member
        AccessError -- Occurs when auth_user_id does not exist

    Return value:
        Empty dictionary
        
    """
    store = data_store.get()

    all_dms = store['dms']
    dm_match = False
    user_match = False
    for dm in range(len(all_dms)):
        if dm_id == all_dms[dm]['dm_id']:
            dm_match = True
        for member in range(len(all_dms[dm]['all_members'])):
            if all_dms[dm]['all_members'][member]['u_id'] == auth_user_id:
                del all_dms[dm]['all_members'][member]
                user_match = True
                break 
        for owner in range(len(all_dms[dm]['owner_members'])):
            if all_dms[dm]['owner_members'][owner]['u_id'] == auth_user_id:
                del all_dms[dm]['owner_members'][owner]
                break 
        if dm_match:
            break 
                
    if not dm_match:
        raise InputError(description="Invalid dm_id")
    elif not user_match:
        raise AccessError(description="User not a member of given DM")
            
    return {}
    
def dm_messages_v1(auth_user_id, dm_id, start):
    """
    Given an auth_user_id, dm_id, and start returns a list of messages and the start and end index of the list
    
    Arguments:
        auth_user_id (integer) -- user who is requesting the DM messages
        dm_id (integer) -- id of the DM
        start (integer) -- starting index of the returned list

    Exceptions:
        InputError -- Occurs when dm_id does not refer to a valid DM 
        InputError -- Occurs when start is greater than the total number of messages in the channel
        AccessError -- Occurs when dm_id is valid and the authorised user is not a member of the DM

    Return value:
        Returns a list of a most 50 messages, the starting index and 
        the ending index (which is -1 if there are less than 50 messages returned)
        
    """
    store = data_store.get()
    dms = store['dms']
    
    curr_dm = {}
    valid_dm_id = False
    valid_access = False
    
    
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
        
    
    # Checks if starting index is outside expected bounds
    if len(curr_dm['messages']) < start or start < 0:
        raise InputError(description="Starting index exceeds bounds of message list")
    
    num_msg_loaded = 0
    messages = []
    end = start + 50
    
    for x in range(len(curr_dm['messages'])):
        if x >= start:
            num_msg_loaded = num_msg_loaded + 1
            if auth_user_id in curr_dm['messages'][x]['reacts'][0]['u_ids']:
                curr_dm['messages'][x]['reacts'][0]['is_this_user_reacted'] = True
            else:
                curr_dm['messages'][x]['reacts'][0]['is_this_user_reacted'] = False
            messages.append(curr_dm['messages'][x])
            
            if num_msg_loaded == 50:
                break
    
    if num_msg_loaded != 50:
        end = -1
        
    return {
        "messages": messages,
        "start": start,
        "end": end,
    }
    
