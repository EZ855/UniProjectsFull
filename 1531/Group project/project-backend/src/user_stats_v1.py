from src.data_store import data_store
from datetime import datetime, timezone

def get_users_stats():
    store = data_store.get()
    return store['workspace_stats']
    

def get_user_stats(u_id):
    store = data_store.get()
    users = store['users']
    for user in users:
        if user['u_id'] == u_id:
            return user['stats']

def channels_check():
    store = data_store.get()
    users = store['users']
    channels = store['channels']
    workspace_stats = store['workspace_stats']
    num_channels_exist = 0
    
    for channel in channels:
        num_channels_exist += 1
    workspace_stats['channels_exist'][0]['num_channels_exist'] = num_channels_exist
    workspace_stats['channels_exist'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
    
    for user in users:
        channels_joined = 0
        for channel in channels:
            if channel['owner_members'] and channel['all_members']:
                for member_list in (channel['owner_members'], channel['all_members']):
                    if channels_joined != 0:
                        break
                    for member in member_list:
                        if member['u_id'] == user['u_id']:
                            channels_joined += 1
                            break
                for key in user:
                    if key == 'stats':
                        user['stats']['channels_joined'][0]['num_channels_joined'] = channels_joined
                        user['stats']['channels_joined'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
                        data_store.set(store) 
                        involvement_calculation(user)
                        utilization_calculation()
    
    
def dms_check():
    store = data_store.get()
    users = store['users']
    dms = store['dms']
    workspace_stats = store['workspace_stats']
    num_dms_exist = 0
    for dm in dms:
        num_dms_exist += 1
    workspace_stats['dms_exist'][0]['num_dms_exist'] = num_dms_exist
    workspace_stats['dms_exist'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
    
    for user in users:
        dms_joined = 0
        for dm in dms:
            if dm['owner_members'] and dm['all_members']:
                for member_list in (dm['owner_members'], dm['all_members']):
                    if dms_joined != 0:
                        break
                    for member in member_list:
                        if member['u_id'] == user['u_id']:
                            dms_joined += 1
                            break
                for key in user:
                    if key == 'stats':
                        user['stats']['dms_joined'][0]['num_dms_joined'] = dms_joined
                        user['stats']['dms_joined'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
                        data_store.set(store)
                        involvement_calculation(user)
                        utilization_calculation()
        


def stat_message_add(u_id):
    store = data_store.get()
    users = store['users']
    workspace_stats = store['workspace_stats']
    
    for user in users:
        if user['u_id'] == u_id:
            user['stats']['messages_sent'][0]['num_messages_sent'] += 1
            user['stats']['messages_sent'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
            workspace_stats['messages_exist'][0]['num_messages_exist'] += 1
            workspace_stats['messages_exist'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
            data_store.set(store) 
            involvement_calculation(user)
            utilization_calculation()
  
def stat_message_remove():
    store = data_store.get()
    users = store['users']
    workspace_stats = store['workspace_stats']
    workspace_stats['messages_exist'][0]['num_messages_exist'] -= 1
    workspace_stats['messages_exist'][0]['time_stamp'] = int(datetime.now(timezone.utc).timestamp())
    data_store.set(store)
    utilization_calculation() 
    for user in users:
        involvement_calculation(user)
    
def involvement_calculation(user):
    store = data_store.get()
    users = store['users']
    workspace_stats = store['workspace_stats']
    
    for user1 in users:
        if user1 == user:
            numerator = user['stats']['channels_joined'][0]['num_channels_joined'] + user['stats']['dms_joined'][0]['num_dms_joined'] + user['stats']['messages_sent'][0]['num_messages_sent']
            denominator = workspace_stats['channels_exist'][0]['num_channels_exist'] + workspace_stats['dms_exist'][0]['num_dms_exist'] + workspace_stats['messages_exist'][0]['num_messages_exist']
            if denominator == 0:
                user['stats']['involvement_rate'] = 0.0
            elif numerator/denominator >= 1.0:
                user['stats']['involvement_rate'] = 1.0
            else:
                user['stats']['involvement_rate'] = numerator/denominator
                
            data_store.set(store)

def utilization_calculation():
    store = data_store.get()
    users = store['users']
    workspace_stats = store['workspace_stats']
    channels = store['channels']
    dms = store['dms']
    num_users = 0
    num_users_in_a_chan_or_dm = 0
    
    for user in users:
        num_users += 1
        user_in_a_chan_or_dm = False
        for channel in channels:
            if user_in_a_chan_or_dm:
                    break
            for member_list in (channel['owner_members'], channel['all_members']):
                if user_in_a_chan_or_dm:
                    break
                for member in member_list:
                    if user['u_id'] == member['u_id']:
                        user_in_a_chan_or_dm = True
                        break
                        
        for dm in dms:
            if user_in_a_chan_or_dm:
                    break
            for member_list in (dm['owner_members'], dm['all_members']):
                if user_in_a_chan_or_dm:
                    break
                for member in member_list:
                    if user['u_id'] == member['u_id']:
                        user_in_a_chan_or_dm = True
                        break
        if user_in_a_chan_or_dm:
            num_users_in_a_chan_or_dm += 1
                        
        
    if num_users == 0:
        workspace_stats['utilization_rate'] = 0.0
    elif num_users_in_a_chan_or_dm/num_users >= 1.0:
        workspace_stats['utilization_rate'] = 1.0
    else:
        workspace_stats['utilization_rate'] = num_users_in_a_chan_or_dm/num_users
        
    data_store.set(store)