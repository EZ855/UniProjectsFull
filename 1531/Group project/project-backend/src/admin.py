from src.data_store import data_store
from src.error import InputError, AccessError

def user_remove(u_id, auth_user):
    store = data_store.get()
    users = store['users']
    channels = store['channels']
    dms = store['dms']
    global_owners = store['global_owners']
    
    
    user_match = False
    for user in users:
        if user['u_id'] == u_id:
            user_match = True
    
    if not user_match:
        raise InputError(description="User id not found")
    
    if auth_user not in global_owners:
        raise AccessError(description="Authorised user is not global owner")
        
    if len(global_owners) == 1 and u_id in global_owners:
        raise InputError(description="User is the only global owner")
    
    if len(channels) > 0:
        for channel in range(len(channels)):
            for channel_member in range(len(channels[channel]['all_members'])):
                if channels[channel]['all_members'][channel_member]['u_id'] == u_id:
                    del channels[channel]['all_members'][channel_member]
                    break
                 
            for channel_member in range(len(channels[channel]['owner_members'])):
                if channels[channel]['owner_members'][channel_member]['u_id'] == u_id:
                    del channels[channel]['owner_members'][channel_member]
                    break
           
            if len(channels[channel]['messages']) > 0:
                for channel_message in range(len(channels[channel]['messages'])):
                    if channels[channel]['messages'][channel_message]['u_id'] == u_id:
                        channels[channel]['messages'][channel_message]['message']  = 'Removed user' 
                        


    for user in users:
        if user['u_id'] == u_id:
            user['email'] = ""
            user['password'] = ""
            user["handle_str"] = ""
            user["name_first"] = "Removed"
            user["name_last"] = "user" 
            user["session_id"] = []       
                                       
    if len(dms) > 0:
        for dm in range(len(dms)):
            for dm_member in range(len(dms[dm]['all_members'])):
                if dms[dm]['all_members'][dm_member]['u_id'] == u_id:
                    del dms[dm]['all_members'][dm_member]
                    break
                    
            for dm_member in range(len(dms[dm]['owner_members'])):
                if dms[dm]['owner_members'][dm_member]['u_id'] == u_id:
                    del dms[dm]['owner_members'][dm_member]
                    break
           
            if len(dms[dm]['messages']) > 0:
                for dm_message in range(len(dms[dm]['messages'])):
                    if dms[dm]['messages'][dm_message]['u_id'] == u_id:
                        dms[dm]['messages'][dm_message]['message']  = 'Removed user'     
    
    if u_id in global_owners:
        global_owners.remove(u_id)
    
    store['deleted_users'].append(u_id)
    
def user_permission_change(u_id, auth_user, permission_id):
    store = data_store.get()
    users = store['users']
    global_owners = store['global_owners']

    user_match = False
    for user in users:
        if user['u_id'] == u_id:
            user_match = True
    
    if not user_match:
        raise InputError(description="User id not found")
    
    if auth_user not in global_owners:
        raise AccessError(description="Authorised user is not global owner")

    if len(global_owners) == 1 and u_id in global_owners and permission_id == 2:
        raise InputError(description="User is the only global owner")
        
    if permission_id not in (1, 2):
        raise InputError(description="Invalid permission_id")
    
    if permission_id == 2:
        global_owners.remove(u_id)
    elif u_id not in global_owners:
        global_owners.append(u_id)
        
