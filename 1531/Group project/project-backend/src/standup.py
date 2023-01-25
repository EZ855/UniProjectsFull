from src.data_store import data_store
from src.error import InputError, AccessError
from datetime import timezone
import datetime
import threading
import time
from src.message import message_send_v1
from src.user_stats_v1 import stat_message_add
def standup_start_v1(auth_user_id, channel_id, length):
    store = data_store.get()
    cha_id_list=[]
    u_id_allmember_list=[]
    time_finish = int(time.time())+length
    for cha in store['channels']:
        cha_id_list.append(cha['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError(description="Invalid channel id")
    if length <= 0:
        raise InputError(description="length must greater than zero!")
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    if auth_user_id not in u_id_allmember_list:
        raise AccessError(description="You are not a member of this channel!")

    for ch in store['channels']:
        if channel_id == ch['channel_id']:
            if 'buffer' in ch:
                raise InputError(description="An active standup is currently running in the channel")
            else:
                ch['buffer'] = ''
                ch['time_finish'] = time_finish
    '''append the buffer area in this channel'''	
    data_store.set(store)
    return{'time_finish':time_finish}
    #t = threading.Timer(length, standup_finish, args=[auth_user_id, channel_id])
    #t.start()
    #data_store.set(store)
    
def standup_finish(auth_user_id, channel_id):
    store = data_store.get()	
    for chan in store['channels']:
        if channel_id == chan['channel_id']:
            if 'buffer' in chan:
                if chan['buffer'] != '':
                    message_send_v1(auth_user_id, channel_id, chan['buffer'])
                    #Send the buffered message as one message if it is not empty
    for channell in store['channels']:
        if channel_id == channell['channel_id']:
            if 'buffer' in channell:
                del chan['buffer']
                del chan['time_finish']
    '''Remove the buffer area in this channel'''
    data_store.set(store)
    stat_message_add(auth_user_id)
    return {}
def buffer_detail(channel_id):
    store = data_store.get()
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            if 'buffer' in channel:
                buffer = channel['buffer']
            else:
                raise InputError(description="There is no standup at this moment")
    data_store.set(store)
    return(buffer)
    
def standup_active_v1(auth_user_id, channel_id):
    store = data_store.get()	
    cha_id_list=[]
    u_id_allmember_list=[]
    for cha in store['channels']:
        cha_id_list.append(cha['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError(description="Invalid channel id")
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    if auth_user_id not in u_id_allmember_list:
        raise AccessError(description="You are not a member of this channel!")
    
    for ch in store['channels']:
        if channel_id == ch['channel_id']:
            if 'buffer' in ch:
                is_active = True
                time_finish = ch['time_finish']
            if 'buffer' not in ch:
                is_active = False
                time_finish = None
    data_store.set(store)
    return{'is_active':is_active, 'time_finish':time_finish}          
def standup_send_v1(auth_user_id, channel_id, message):
    store = data_store.get()
    cha_id_list=[]
    u_id_allmember_list=[]
    for cha in store['channels']:
        cha_id_list.append(cha['channel_id'])
    if channel_id not in cha_id_list:
        raise InputError(description="Invalid channel id")
    if len(message)<1 or len(message)>1000:
        raise InputError(description="message length too long or short")
    returns = standup_active_v1(auth_user_id, channel_id)
    if returns['is_active'] == False:
        raise InputError(description="Standup is not currently running in the channel")
    for channel in store['channels']:
        if channel_id == channel['channel_id']:
            for member in channel['all_members']:
                u_id_allmember_list.append(member['u_id'])
    if auth_user_id not in u_id_allmember_list:
        raise AccessError(description="You are not a member of this channel!")
    #store['message_id_curr'] = store['message_id_curr'] + 1 
    
    handle_str = auth_user_handle_str(auth_user_id)  
    for chan in store['channels']:
        if channel_id == chan['channel_id']:
            chan['buffer'] +=  handle_str + ': ' + message +'\n'
    stat_message_add(auth_user_id)
    return{}
def auth_user_handle_str(auth_user_id):
    store = data_store.get()
    for user in store['users']:
        if user['u_id'] == auth_user_id:
            handle_str = user['handle_str']
    return handle_str
		
