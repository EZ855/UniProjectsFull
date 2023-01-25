import hashlib
import jwt
import uuid 
import re
import string, random
from src.error import AccessError, InputError
from jwt import DecodeError
from src.data_store import data_store

REGEX = r'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$'
SECRET = 'montypython193798279187549782monty93793784python29397492743927'

def new_session_id():
    return str(uuid.uuid4())

def generate_reset_code():
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=6))
    
def hash_string(input_string):
    return hashlib.sha256(input_string.encode()).hexdigest()

def new_jwt(auth_user_id, session_id=None):
    if session_id == None:
        session_id = new_session_id()
    return jwt.encode({'auth_user_id': auth_user_id, 'session_id': session_id}, SECRET, algorithm='HS256')

def check_valid_session(auth_user_id, session_id, users):
    match = False 
    for user in users:
        if auth_user_id == user['u_id']:
            match = True
            if session_id not in user['session_id']:
                raise AccessError(description = "Invalid session id")
            break
    if not match:
        raise AccessError(description = "Invalid session id")

def decode_jwt(encoded_jwt):
    try:
        token = jwt.decode(encoded_jwt, SECRET, algorithms=['HS256'])
    except DecodeError as Error:
        raise AccessError from Error
    store = data_store.get()
    check_valid_session(token['auth_user_id'], token['session_id'], store['users'])
    return token
    
def store_session_id(auth_user_id, session_id, users):
    for user in users:
        if user['u_id'] == auth_user_id:
            if 'session_id' in user:
                user['session_id'].append(session_id)
            else: 
                user['session_id'] = [session_id]

def remove_session(auth_user_id, session_id, users):
    for user in users:
        if auth_user_id == user['u_id']:
            user['session_id'].remove(session_id)
            break

def remove_all_sessions(auth_user_id, session_id, users):
    for user in users:
        if auth_user_id == user['u_id']:
            user['session_id'] = []
            break
 
def name_swap(u_id, name_first, name_last, users):
    if not 1 <= len(name_first) <= 50:
        raise InputError(description = "Invalid first name")
    if not 1<= len(name_last) <= 50:
        raise InputError(description = "Invalid last name")
    for user in users:
        if user['u_id'] == u_id:
            user['name_first'] = name_first
            user['name_last'] = name_last
            break
            
def email_swap(u_id, email, users):
    if not re.fullmatch(REGEX, email):
        raise InputError(description = "Invalid email")

    for user in users:
        if user['email'] == email:
            raise InputError(description = "Email already taken")

    for user in users:
        if user['u_id'] == u_id:
            user['email'] = email
            break

    for user in users:
        if user['email'] == email and user['u_id'] != u_id:
            raise InputError(description = "Email is already in use by another user")


def handle_swap(u_id, new_handle, users):
    if not new_handle.isalnum():
        raise InputError(description = "Handle contains non-alphanumeric characters")

    for user in users:
        if user['handle_str'] == new_handle and user['u_id'] != u_id:
            raise InputError(description = "Handle already taken")

    if not 3<= len(new_handle) <= 20:
        raise InputError(description = "Invalid handle")

    for user in users:
        if user['u_id'] == u_id:
            user['handle_str'] = new_handle
            break          
