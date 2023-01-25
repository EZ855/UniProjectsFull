import sys
import signal
from json import dumps
from flask import Flask, request, send_from_directory
from flask_cors import CORS
from src.error import InputError, AccessError
from src import config
from src.photo import photo_download, photo_crop, store_user_photo, type_check, dimensions_check

from src.helpers import SECRET, check_valid_session, decode_jwt
from src.dm import dm_create_v1, dm_list_v1, dm_remove_v1, dm_details_v1, dm_leave_v1, dm_messages_v1
from src.user_stats_v1 import channels_check, dms_check, stat_message_add, get_user_stats, stat_message_remove, get_users_stats
from src.notifications import return_notifications
from src.search import search_string
from src.auth import auth_register_v1, auth_login_v1
from src.channel import channel_messages_v1
from src.data_store import data_store
from src.helpers import new_session_id, new_jwt, store_session_id, decode_jwt, generate_reset_code, hash_string, remove_all_sessions
from src.message import message_send_v1, message_remove_v1, message_edit_v1, message_senddm_v1, message_sendlater_v1, message_share_v1, message_sendlaterdm_v1, message_react_v1, message_unreact_v1, message_pin_v1, message_unpin_v1
from src.helpers import remove_session, name_swap, email_swap, handle_swap
from src.other import clear_v1
from src.channels import channels_create_v1, channels_list_v1, channels_listall_v1
from src.channel import channel_details_v1
from src.channel import channel_leave_v1
from src.channel import channel_join_v1
from src.channel import channel_invite_v1
from src.channel import channel_addowner_v1
from src.channel import channel_removeowner_v1
from src.admin import user_remove, user_permission_change
from src.standup import standup_start_v1,standup_active_v1, standup_send_v1,standup_finish
import json
import os
import threading
import time

from flask_mail import Mail, Message

def quit_gracefully(*args):
    '''For coverage'''
    exit(0)

def defaultHandler(err):
    response = err.get_response()
    print('response', err, err.get_response())
    response.data = dumps({
        "code": err.code,
        "name": "System Error",
        "message": err.get_description(),
    })
    response.content_type = 'application/json'
    return response

APP = Flask(__name__, static_url_path='/static/')
CORS(APP)

# https://www.twilio.com/blog/2018/03/send-email-programmatically-with-gmail-python-and-flask.html
mail_settings = {
    "MAIL_SERVER": 'smtp.gmail.com',
    "MAIL_PORT": 465,
    "MAIL_USE_TLS": False,
    "MAIL_USE_SSL": True,
    "MAIL_USERNAME": 'pythonmonty302@gmail.com',
    "MAIL_PASSWORD": 'montypython19974875'
}

APP.config.update(mail_settings)
mail = Mail(APP)


APP.config['TRAP_HTTP_EXCEPTIONS'] = True
APP.register_error_handler(Exception, defaultHandler)

#### NO NEED TO MODIFY ABOVE THIS POINT, EXCEPT IMPORTS

# Example
@APP.route("/echo", methods=['GET'])
def echo():
    data = request.args.get('data')
    if data == 'echo':
   	    raise InputError(description='Cannot echo "echo"')
    return dumps({
        'data': data
    })

def save_data():
    data = data_store.get()
    with open('database.json', 'w') as FILE:
        json.dump(data, FILE)

def load_data():
    with open('database.json', 'r') as FILE:
        data = json.load(FILE)
        data_store.set(data)
    store = data_store.get()
    users = store['users']
    hash_code = store['hash_code']
    print(users, hash_code)
    for user in users:
        print(user.get('email'))
        hash_code[user.get('email')] = ''

def save_every_5_seconds():
    while True:
        time.sleep(5)
        save_data()

"""
Loads stored data in database.json, creates file if file doesn't exist.
"""
cwd = os.getcwd()
if os.path.isfile(f"{cwd}/database.json"):
    load_data()
else: 
    save_data() 
    
"""
Creates a thread for running the saving loop
"""
thread = threading.Thread(target=save_every_5_seconds)
thread.start()

@APP.route('/static/<path:path>')
def send_js(path):
    return send_from_directory('', path)

@APP.route("/user/profile/uploadphoto/v1", methods=['POST'])
def photo_upload():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    img_url = info['img_url']
    x_start = info['x_start']
    y_start = info['y_start']
    x_end = info['x_end']
    y_end = info['y_end']
    i = len(img_url) - 1
    while(img_url[i] != '.'):
        i -= 1
    ending = img_url[i + 1:]
    photo_download(img_url, auth_user_id, ending)
    file_path = f"src/static/{auth_user_id}.{ending}"
    dimensions_check(file_path, x_start, y_start, x_end, y_end)  
    type_check(ending)
    photo_crop(file_path, x_start, y_start, x_end, y_end)
    store_user_photo(auth_user_id, ending)
    return dumps({})


# https://www.twilio.com/blog/2018/03/send-email-programmatically-with-gmail-python-and-flask.html
@APP.route("/auth/passwordreset/request/v1", methods=['POST'])
def send_mail():
    store = data_store.get()
    data = request.get_json()
    token = decode_jwt(data['token'])
    email = data['email']
    users = store['users']
    session_id = token['session_id']
    u_id = token['auth_user_id']
    reset_code = generate_reset_code()
    code_hash = hash_string(reset_code)
    if email in store['hash_code']:
        store['hash_code'][email] = code_hash
    msg = Message(subject="Password change request",
        sender = "pythonmonty302@gmail.com",
        recipients = [f"<{email}>"], # replace with your email for testing
        body = f"The code required to change your password is {reset_code}")
    mail.send(msg)
    remove_all_sessions(u_id, session_id, users)
    return 'Mail sent'

@APP.route("/auth/passwordreset/reset/v1", methods = ['POST'])
def reset_password():
    store = data_store.get()
    data = request.get_json()
    reset_code = data['reset_code'] 
    new_password = data['new_password']
    code_hash = hash_string(reset_code)
    for email, h_string in enumerate(store['hash_code']):
        if h_string == code_hash:
            for user in store['users']:
                if user['email'] == email:
                    user['password'] = new_password


@APP.route("/search/v1", methods=['GET'])
def search_v1():
    store = data_store.get()
    token = decode_jwt(request.args.get('token'))
    auth_user_id = int(token['auth_user_id'])
    query_string = request.args.get('query_str')
    messages = search_string(auth_user_id, query_string, store['channels'], store['dms'])
    return dumps({"messages" : messages})

@APP.route("/notifications/get/v1", methods=['GET']) 
def notification_http(): 
    token = decode_jwt(request.args.get('token'))
    auth_user_id = int(token['auth_user_id'])
    store = data_store.get()
    notifications = store['notifications']   
    return dumps(return_notifications(auth_user_id, notifications))

@APP.route("/user/stats/v1", methods=['GET'])
def user_stats_v1():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = int(token['auth_user_id'])
    stats = get_user_stats(auth_user_id)
    return dumps(stats)

@APP.route("/users/stats/v1", methods=['GET'])
def users_stats_v1():
    decode_jwt(request.args.get('token'))
    stats = get_users_stats()
    return dumps(stats)

@APP.route("/channels/create/v2", methods=['POST'])
def channels_create_v2():
    data = request.get_json()
    token = decode_jwt(data['token'])
    name = data['name']
    is_public = data['is_public']
    auth_user_id = token['auth_user_id']
    channel = channels_create_v1(auth_user_id, name, is_public)
    channels_check()
    return dumps({
            'channel_id': channel['channel_id']
            })
            
@APP.route("/channels/list/v2", methods=['GET'])
def channels_list():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = int(token['auth_user_id'])
    channels = channels_list_v1(auth_user_id)
    return dumps(channels)
    
@APP.route("/channels/listall/v2", methods=['GET'])
def channels_listall():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = int(token['auth_user_id'])
    channels = channels_listall_v1(auth_user_id)
    return dumps(channels)

@APP.route("/channel/details/v2", methods=['GET'])
def channels_details_v2():
    token = request.args.get('token')
    channel_id = int(request.args.get('channel_id'))
    auth_user_id = decode_jwt(token)['auth_user_id']
    details = channel_details_v1(auth_user_id, channel_id)
    return dumps(details)



@APP.route("/dm/create/v1", methods=['POST'])
def dm_create():
    data = request.get_json()
    token = decode_jwt(data['token'])
    auth_user_id = token['auth_user_id']
    u_ids = data['u_ids']
    dm_id = dm_create_v1(auth_user_id, u_ids)
    dms_check()
    return dumps({
        'dm_id': dm_id
    })
    
@APP.route("/dm/list/v1", methods=['GET'])
def dm_list():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = token['auth_user_id']
    dms = dm_list_v1(auth_user_id)
    return dumps(dms)
    
@APP.route("/dm/remove/v1", methods=['DELETE'])
def dm_remove():
    data = request.get_json()
    token = decode_jwt(data['token'])
    auth_user_id = token['auth_user_id']
    dm_id = data['dm_id']
    dm_remove_v1(auth_user_id, dm_id)
    stat_message_remove()
    dms_check()
    return dumps({
    })

@APP.route("/dm/details/v1", methods=['GET'])
def dm_details():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = int(token['auth_user_id'])
    dm_id = int(request.args.get('dm_id'))
    details = dm_details_v1(auth_user_id, dm_id)
    return dumps(details
    )

@APP.route("/dm/leave/v1", methods=['POST'])
def dm_leave():
    data = request.get_json()
    token = decode_jwt(data['token'])
    auth_user_id = token['auth_user_id']
    dm_id = data['dm_id']
    dm_leave_v1(auth_user_id, dm_id)
    dms_check()
    return dumps({
    })

@APP.route("/message/react/v1", methods=['POST'])
def messsage_react():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_react_v1(auth_user_id, info['message_id'], info['react_id'])
    return dumps({})
    
@APP.route("/message/unreact/v1", methods=['POST'])
def messsage_unreact():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_unreact_v1(auth_user_id, info['message_id'], info['react_id'])
    return dumps({})

@APP.route("/message/pin/v1", methods=['POST'])
def messsage_pin():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_pin_v1(auth_user_id, info['message_id'])
    return dumps({})

@APP.route("/message/unpin/v1", methods=['POST'])
def messsage_unpin():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_unpin_v1(auth_user_id, info['message_id'])
    return dumps({})

@APP.route("/message/senddm/v1", methods=['POST'])
def dm_send():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_id = message_senddm_v1(auth_user_id, info['dm_id'], info['message'])
    stat_message_add(auth_user_id)
    return dumps({'message_id': message_id['message_id']})

@APP.route("/message/sendlaterdm/v1", methods=['POST'])
def sendlaterdm():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_id = message_sendlaterdm_v1(auth_user_id, info['dm_id'], info['message'], info['time_sent'])
    stat_message_add(auth_user_id)
    return dumps(message_id)

@APP.route("/dm/messages/v1", methods=['GET'])
def dm_messages():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = token['auth_user_id']
    dm_id = int(request.args.get('dm_id'))
    start = int(request.args.get('start'))
    dm_dict = dm_messages_v1(auth_user_id, dm_id, start)
    return dumps(dm_dict)    

@APP.route("/auth/register/v2", methods=['POST'])
def register():
    store = data_store.get()
    info = request.get_json()
    u_id = auth_register_v1(info['email'], info['password'], info['name_first'], info['name_last'])['auth_user_id']
    session_id = new_session_id()
    store_session_id(u_id, session_id, store['users'])
    return dumps({
        'token': new_jwt(u_id, session_id), 
        'auth_user_id': u_id
    })

@APP.route("/auth/login/v2", methods=['POST'])
def login():
    store = data_store.get()
    info = request.get_json()
    u_id = auth_login_v1(info['email'],info['password'])['auth_user_id']
    session_id = new_session_id()
    store_session_id(u_id, session_id, store['users'])
    return dumps({
        'token': new_jwt(u_id, session_id), 
        'auth_user_id': u_id
    })

@APP.route("/channel/messages/v2", methods=['GET'])
def messages():
    token = decode_jwt(request.args.get('token'))
    auth_user_id = token['auth_user_id']
    channel_id = int(request.args.get('channel_id'))
    start = int(request.args.get('start'))
    messages_dict = channel_messages_v1(auth_user_id, channel_id, start)
    return dumps(messages_dict)


@APP.route("/message/send/v1", methods=['POST'])
def send():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_id = message_send_v1(auth_user_id, info['channel_id'], info['message'])
    stat_message_add(auth_user_id)
    return dumps({'message_id': message_id['message_id']})
    
@APP.route("/message/share/v1", methods=['POST'])
def share_message():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    shared_message_id = message_share_v1(auth_user_id, info['og_message_id'], info['message'], info['channel_id'], info['dm_id'])
    stat_message_add(auth_user_id)
    return dumps({'shared_message_id': shared_message_id['shared_message_id']})

@APP.route("/message/sendlater/v1", methods=['POST'])
def sendlater():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_id = message_sendlater_v1(auth_user_id, info['channel_id'], info['message'], info['time_sent'])
    stat_message_add(auth_user_id)
    return dumps(message_id)

@APP.route("/message/remove/v1", methods=['DELETE'])
def remove():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_remove_v1(auth_user_id, info['message_id'])
    stat_message_remove()
    return dumps({})
    
@APP.route("/message/edit/v1", methods=['PUT'])
def edit():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    message_edit_v1(auth_user_id, info['message_id'], info['message'])
    return dumps({})
      
@APP.route("/clear/v1", methods=['DELETE'])
def clear():
    clear_v1()
    return dumps({})

@APP.route("/auth/logout/v1", methods=['POST'])
def logout():
    store = data_store.get()
    info = request.get_json()
    token = decode_jwt(info['token'])
    users = store['users']
    session_id = token['session_id']
    u_id = token['auth_user_id']
    remove_session(u_id, session_id, users)
    return dumps({})

@APP.route("/users/all/v1", methods=['GET'])
def users_all():
    data = request.args.get('token')
    decode_jwt(data)
    store = data_store.get()
    users = []
    for user in store['users']:
        if user["u_id"] not in store['deleted_users']:
            users.append({"u_id": user['u_id'], "email": user['email'], "name_first": user['name_first'],
             "name_last": user['name_last'], "handle_str": user['handle_str'], "profile_img_url": user['profile_img_url']})
    return dumps({'users': users})
   
@APP.route("/user/profile/v1", methods=['GET']) 
def user_profile():
    token = request.args.get('token')
    u_id = int(request.args.get("u_id"))
    decode_jwt(token) 
    store = data_store.get()
    user_data = {}
    match = False
    for user in store['users']:
        if user['u_id'] == u_id:
            user_data = {"u_id": user['u_id'], "email": user['email'], "name_first": user['name_first'],
            "name_last": user['name_last'], "handle_str": user['handle_str'], "profile_img_url": user['profile_img_url']}
            match = True
            break
    if not match:
        raise InputError("Invalid user id")
    return dumps({'user': user_data})
         
@APP.route("/user/profile/setname/v1", methods=['PUT'])
def user_profile_setname():
    store = data_store.get()
    info = request.get_json()
    token = decode_jwt(info["token"])
    u_id = token["auth_user_id"]
    name_first = info['name_first']
    name_last = info['name_last']
    users = store['users']
    name_swap(u_id, name_first, name_last, users)
    return dumps({})
            
@APP.route("/user/profile/setemail/v1", methods = ['PUT'])
def user_profile_setemail():
    store = data_store.get()
    info = request.get_json()
    token = decode_jwt(info["token"])
    u_id = token["auth_user_id"]
    email = info['email']
    users = store['users']
    email_swap(u_id, email, users)
    return dumps({})

@APP.route("/user/profile/sethandle/v1", methods = ['PUT'])
def user_profile_sethandle():
    store = data_store.get()
    info = request.get_json()
    token = decode_jwt(info["token"])
    u_id = token["auth_user_id"]
    handle = info['handle_str']
    users = store['users']
    handle_swap(u_id, handle, users)
    return dumps({})

@APP.route("/channel/leave/v1", methods=['POST'])
def channel_leave():
    info = request.get_json()
    token = decode_jwt(info['token'])
    channel_id = info['channel_id']
    auth_user_id = token['auth_user_id']
    returns = channel_leave_v1(auth_user_id, channel_id)
    channels_check()
    return dumps(returns)

@APP.route("/channel/join/v2", methods = ['POST'])
def channel_join():
    info = request.get_json()
    token = decode_jwt(info['token'])
    channel_id = info['channel_id']
    auth_user_id = token['auth_user_id']
    returns = channel_join_v1(auth_user_id, channel_id)
    channels_check()
    return dumps(returns)

@APP.route("/channel/invite/v2", methods = ['POST'])
def channel_invite():
    info = request.get_json()
    token = decode_jwt(info['token'])
    channel_id = info['channel_id']
    auth_user_id = token['auth_user_id']
    u_id = info['u_id']
    returns = channel_invite_v1(auth_user_id, channel_id, u_id)
    channels_check()
    return dumps(returns)

@APP.route("/channel/addowner/v1", methods = ['POST'])
def channel_addowner():
    info = request.get_json()
    token = decode_jwt(info['token'])
    channel_id = info['channel_id']
    auth_user_id = token['auth_user_id']
    u_id = info['u_id']
    returns = channel_addowner_v1(auth_user_id, channel_id, u_id)
    channels_check()
    return dumps(returns)

@APP.route("/channel/removeowner/v1", methods = ['POST'])
def channel_removeowner():
    info = request.get_json()
    token = decode_jwt(info['token'])
    channel_id = info['channel_id']
    auth_user_id = token['auth_user_id']
    u_id = info['u_id']
    returns = channel_removeowner_v1(auth_user_id, channel_id, u_id)
    channels_check()
    return dumps(returns)
    
@APP.route("/admin/user/remove/v1", methods=['DELETE']) 
def admin_user_remove():
    info = request.get_json()
    token = decode_jwt(info['token'])   
    auth_user = token['auth_user_id']
    u_id = info['u_id']
    user_remove(u_id, auth_user)
    channels_check()
    dms_check()
    return dumps({})
  
@APP.route("/admin/userpermission/change/v1", methods=['POST']) 
def admin_permission_change():
    info = request.get_json()
    token = decode_jwt(info['token'])   
    auth_user = token['auth_user_id']
    u_id = info['u_id']
    permission_id = info['permission_id']
    user_permission_change(u_id, auth_user, permission_id)
    return dumps({})
@APP.route("/standup/start/v1", methods=['POST'])
def standup_start():    
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    channel_id = info['channel_id']
    length = info['length']
    returns = standup_start_v1(auth_user_id, channel_id, length)
    t = threading.Timer(length, standup_finish, args=[auth_user_id, channel_id])
    t.start()
    return dumps(returns)

@APP.route("/standup/active/v1", methods=['GET'])
def standup_active():
    
    token = request.args.get('token')
    channel_id = int(request.args.get('channel_id'))
    auth_user_id = decode_jwt(token)['auth_user_id']
    returns = standup_active_v1(auth_user_id, channel_id)
    return dumps(returns)
    

@APP.route("/standup/send/v1", methods=['POST']) 
def standup_send():
    info = request.get_json()
    token = decode_jwt(info['token'])
    auth_user_id = token['auth_user_id']
    channel_id = info['channel_id']
    message = info['message']
    standup_send_v1(auth_user_id, channel_id, message)
    return dumps({})  

### NO NEED TO MODIFY BELOW THIS POINT

if __name__ == "__main__":
    signal.signal(signal.SIGINT, quit_gracefully) # For coverage
    APP.run(port=config.port) # Do not edit this port
