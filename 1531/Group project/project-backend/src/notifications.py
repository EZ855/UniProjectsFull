from src.data_store import data_store

def find_handle(u_id, users):
    for user in users:
        if u_id == user['u_id']:
            return user['handle_str']

def find_channel_name(channel_id, channels):
    for channel in channels:
        if channel['channel_id'] == channel_id:
            return channel['name']

def notification_channel_invite(auth_user_id, u_id, channel_id):
    store = data_store.get()
    notifications = store['notifications']
    auth_handle = find_handle(auth_user_id, store['users'])
    channel_name = find_channel_name(channel_id, store['channels'])
    message = f"{auth_handle} added you to {channel_name}"
    new = {"channel_id": channel_id, "dm_id": -1, "notification_message": message}
    
    user_found = False 
    for notification in notifications:
        if list(notification.keys()) == [u_id]:
            notification[u_id].insert(0, new)
            user_found = True 
    
    if not user_found:
        notifications.append({u_id: [new]}) 

def find_dm_name(dm_id, dms):
    for dm in dms:
        if dm['dm_id'] == dm_id:
            return dm['name']

def notification_dm_create(auth_user_id, u_id, dm_id):
    store = data_store.get()
    notifications = store['notifications']
    auth_handle = find_handle(auth_user_id, store['users'])
    dm_name = find_dm_name(dm_id, store['dms'])
    message = f"{auth_handle} added you to {dm_name}"
    new = {"channel_id": -1, "dm_id": dm_id, "notification_message": message}
    
    user_found = False 
    for notification in notifications:
        if list(notification.keys()) == [u_id]:
            notification[u_id].insert(0, new)
            user_found = True 
    
    if not user_found:
        notifications.append({u_id: [new]})

def notification_react(auth_user_id, u_id, name, channel_value, dm_value):
    store = data_store.get()
    users = store['users']
    notifications = store['notifications']
    user_handle = find_handle(auth_user_id, users)
    message = f"{user_handle} reacted to your message in {name}"
    new = new = {"channel_id": channel_value, "dm_id": dm_value, "notification_message": message}
    
    user_found = False 
    for notification in notifications:
        if list(notification.keys()) == [u_id]:
            notification[u_id].insert(0, new)
            user_found = True 
    
    if not user_found:
        notifications.append({u_id: [new]})

def return_notifications(auth_user_id, notifications):
    n = []
    match = False 
    for user in notifications:
        if list(user.keys()) == [auth_user_id]:
            match = True
            break 
    if match:
        for notification in user[auth_user_id]:
            n.append({"channel_id": notification["channel_id"], "dm_id": notification["dm_id"], 
                        "notification_message": notification["notification_message"]}) 
    return ({"notifications": n})

