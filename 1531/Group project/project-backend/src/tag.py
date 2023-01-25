from src.notifications import find_handle, find_dm_name, find_channel_name
from src.data_store import data_store

def find_id_from_handle(handle_str):
    store = data_store.get()
    users = store['users']
    for user in users:
        if user['handle_str'] == handle_str:
            return user['u_id']
    return False 


def check_in_channel(u_id, channel_id):
    store = data_store.get() 
    channels = store['channels']
    channel_members = []
    for channel in channels:
        if channel['channel_id'] == channel_id:
            channel_members = [member['u_id'] for member in channel['all_members']]
    if u_id in channel_members:
        return True
    return False 


def check_in_dm(u_id, dm_id):
    store = data_store.get() 
    dms = store['dms']
    dm_members = []
    for dm in dms:
        if dm['dm_id'] == dm_id:
            dm_members = [member['u_id'] for member in dm['all_members']]
    if u_id in dm_members:
        return True
    return False


def tag_user_channel(auth_user_id, u_id, message_id, notifications, channel_id, message):
    if not check_in_channel(u_id, channel_id):
        return
    for user in notifications:
        if list(user.keys()) == [u_id]:
            break
    store = data_store.get()
    
    notification_keys = [list(n.keys()) for n in notifications]
    
    user_handle = find_handle(auth_user_id, store['users'])
    channel_name = find_channel_name(channel_id, store['channels'])
    notification_message = f"{user_handle} tagged you in {channel_name}: {message}"
    new = {"channel_id": channel_id, "dm_id": -1, "message_id": message_id,  "notification_message": notification_message}   
    
    if [u_id] in notification_keys:
        for notification in user[u_id]:
            if notification.get("message_id") == message_id:
                return    
        user[u_id].insert(0, new)
    else:
        notifications.append({u_id: [new]})
    



def tag_user_dm(auth_user_id, u_id, message_id, notifications, dm_id, message): 
    if not check_in_dm(u_id, dm_id):
        return
    for user in notifications:
        if list(user.keys()) == [u_id]:
            break
    
    store = data_store.get()
    user_handle = find_handle(auth_user_id, store['users'])
    dm_name = find_dm_name(dm_id, store['dms'])
    notification_message = f"{user_handle} tagged you in {dm_name}: {message}"
    new = {"channel_id": -1, "dm_id": dm_id, "message_id": message_id, "notification_message": notification_message}
    
    notification_keys = [list(n.keys()) for n in notifications]
    
    if [u_id] in notification_keys: 
        for notification in user[u_id]:
            if notification.get("message_id") == message_id:
                return 
        user[u_id].insert(0, new)
    else:
        notifications.append({u_id: [new]})
    
   
                       
def tag_user(auth_user_id, sentence, message_id, is_channel=False, is_dm=False, ch_id=None, dm_id=None):                       
    index = 0
    store = data_store.get()
    while (index < len(sentence)):
        if sentence[index] == '@':
            handle_str = ''
            index += 1
            while index < len(sentence) and sentence[index].isalnum():
                handle_str += sentence[index]
                index += 1 
            user_id = find_id_from_handle(handle_str)
            if (user_id):
                if (is_channel):
                    tag_user_channel(auth_user_id, user_id, message_id, store['notifications'], ch_id, sentence[:20])
                else:
                    tag_user_dm(auth_user_id, user_id, message_id, store['notifications'], dm_id, sentence[:20])       
        index += 1


