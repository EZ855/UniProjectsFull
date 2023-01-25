from src.data_store import data_store

def clear_v1():
    store = data_store.get()
    store['users'] = []
    store['channels'] = []
    store['message_id_curr'] = 0
    store['dms'] = []
    store['global_owners'] = []
    store['deleted_users'] = []
    store['workspace_stats'] = {
            'channels_exist': [{'num_channels_exist':0, 'time_stamp': 0}], 
            'dms_exist': [{'num_dms_exist':0, 'time_stamp': 0}], 
            'messages_exist': [{'num_messages_exist':0, 'time_stamp': 0}], 
            'utilization_rate': 0 
            }
    store['notifications'] = []
    data_store.set(store)
