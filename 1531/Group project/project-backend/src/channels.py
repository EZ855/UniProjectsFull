from src.data_store import data_store
from src.error import InputError, AccessError



def channels_list_v1(auth_user_id):
    store = data_store.get()
    all_channels = store['channels']
    user_channels = []
    for channel in all_channels:
        for member in channel['all_members']:
            if member['u_id'] == auth_user_id:
                info = {'channel_id': channel['channel_id'], 'name': channel['name']}
                user_channels.append(info)
                break
    return {
        'channels': user_channels
    }

def channels_listall_v1(auth_user_id):
    store = data_store.get()
    all_channels = []
    channels = store['channels']
    users = store['users']
    match = False 
    for user in users:
        if user['u_id'] == auth_user_id:
            match = True     
    if match:          
        for channel in channels:
            info = {'channel_id': channel['channel_id'], 'name': channel['name']}
            all_channels.append(info)
    return {
        'channels': all_channels
    }



def channels_create_v1(auth_user_id, name, is_public):

    """
    Given an auth_user_id, a string between 1 and 20 characters inclusive and a True or False for public and private respectively,
    creates/appends a channel onto the list 'channels' in data_store.py and 
    adds the user and their associated email, first and last name to 'owner_members' and 'all_members' list for that channel.
    
    Arguments:
        auth_user_id (integer) -- user who is creating the channel's ID
        name (string) -- name entered by user
        is_public (boolean) -- status of server being public/private entered by user

    Exceptions:
        InputError -- Occurs when name is <1 character long
        InputError -- Occurs when name is >20 characters long
        AccessError -- Occurs when given auth_user_id doesn't exist

    Return value:
        Returns the created channel_id given a valid name
        """

    store = data_store.get()
    channels = store['channels']
    
    if len(name) == 0:
        raise InputError(description="Channel name length cannot be less than 1 character")
    
    elif len(name) > 20:
        raise InputError(description="Channel name length cannot be more than 20 characters")
    

    users = store['users']
    name_f = ""
    name_l = ""
    email = ""
    handle_str = ""
    match = False
    for user in users:
        if user['u_id'] == auth_user_id:
            name_f = user['name_first']
            name_l = user['name_last']
            email = user['email']
            handle_str = user['handle_str']
            profile_img_url = user["profile_img_url"]
            match = True
            break
    if not match:
        raise AccessError(description="Invalid user id")
    """
    Creates a dictionary (i.e. a channel) in 'channels' list in data_store.py with the following information:
    - 'channel_id':     1 (integer)
    - 'name':           name (string 1-20 characters inclusive)
    - 'is_public:       True/False (True for public, False for private)
    - 'owner_members':  List of dictionaries of types user
    - 'all_members':    List of dictionaries of types user
    - 'messages':       Empty list
    
    If there are already channels that exist, finds the highest channel_id and adds 1 to it. 
    Then creates a channel with that id.
    """
    
    new_id = len(channels) + 1
    channels.append(
        {
            'channel_id': new_id, 
            'name': name, 
            'is_public': is_public, 
            'owner_members': [
                {
                    'u_id': auth_user_id,
                    'email': email,
                    'name_first': name_f,
                    'name_last': name_l,
                    'handle_str': handle_str,
                    "profile_img_url": profile_img_url,
                }
            ],
            'all_members': [
                {
                    'u_id': auth_user_id,
                    'email': email,
                    'name_first': name_f,
                    'name_last': name_l,
                     'handle_str': handle_str,
                     "profile_img_url": profile_img_url,
                 }
            ],
            'messages':[]
       }
    )
    data_store.set(store)       
    return {'channel_id': new_id}

