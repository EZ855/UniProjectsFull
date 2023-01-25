from src.data_store import data_store
from src.error import InputError

def search_string(u_id, string, channels, dms):
    if len(string) < 1 or len(string) > 1000:
        raise InputError(description="Query string has to be between 1 and 1000 characters inclusive")

    m = []
    for channel in channels:
        if u_id in [member['u_id'] for member in channel['all_members']]:
            for message in channel['messages']:
                if string in message["message"]:
                    m.append(message)
                    
    for dm in dms:
        if u_id in [member['u_id'] for member in dm['all_members']]:
            for message in dm['messages']:
                if string in message['message']:
                    m.append(message)
    return m
