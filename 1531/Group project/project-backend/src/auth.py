import re
from src.data_store import data_store
from src.error import InputError
from src import config 
import hashlib

REGEX = r'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$'

def email_check_match(email, users):
    '''
    Checks if email has already been registered to a user.
    If so, returns True. Otherwise returns False.
    '''
    match = False
    if users == []:
        return False 
    for user_data in users:
        if email == user_data['email']:
           match = True
    return match

def password_check_match(email, password, users):
    '''
    Matches user email, and checks if password entered is correct.
    If password is correct, returns True. Otherwise returns False.
    '''
    match = False
    for user_data in users:
        if email == user_data['email'] and password == user_data['password']:
            match = True
    return match

def find_user_id(email, users):
    '''
    Given user email, finds and returns corresponding user id.
    '''
    
    for user_data in users:
        if email == user_data['email']:
            u_id = user_data['u_id']
           
    return u_id

def auth_login_v1(email, password):
    '''
    Given registered user's email and password, returns their 
    user id value.
    
    Arguments: 
        email (string) -- email entered by user 
        password (string) -- password entered by user 
    
    Exceptions:
        InputError -- Occurs when email does not belond to a registered user
        InputError -- Occurs when password is not correct 
        
    Return value: 
        Returns user id value given valid email and corresponding password  
    '''
    store = data_store.get()

    if not email_check_match(email, store['users']):
        raise InputError("Email is not registered")

    password = hashlib.sha256(password.encode()).hexdigest()    
    
    if not password_check_match(email, password, store['users']):
        raise InputError("Incorrect password")

    return {
        'auth_user_id': find_user_id(email, store['users'])
    }

def check_handle_match(handle_str, users):
    '''
    Takes in a handle string, and checks if that handle has
    already been assigned to a user
    '''
    if users != []:
        for user_data in users:
            if handle_str == user_data['handle_str']:
                return True
    return False

def create_new_id(users):
    '''
    Generates and returns a new user id
    '''   
    return len(users) + 1

def auth_register_v1(email, password, name_first, name_last):
    '''
    Given user email, password, first name and last name,
    generates a new account and returns a new user id

    Arguments:
        email (string) -- email entered by user
        password (string) -- password entered by user
        name_first (string) -- first name entered by user
        name_last (string) -- last name entered by user

    Exceptions:
        InputError -- Occurs when email does not match the regular expression
        InputError -- Occurs when email address is being used by another user
        InputError -- Occurs when length of password is < 6 characters
        InputError -- Occurs when length of name_first is not 1 ≤ name_first ≤ 50
        InputError -- Occurs when length of name_last is not 1 ≤ name_last ≤ 50

    Return value:
        Returns auth_user_id given valid inputs
    '''
    if not re.fullmatch(REGEX, email):
        raise InputError("Invalid email")

    if len(password) < 6:
        raise InputError("Invalid password")

    if len(name_first) < 1 or len(name_first) > 50:
        raise InputError("Invalid first name")

    if len(name_last) < 1 or  len(name_last) > 50:
        raise InputError("Invalid last name")

    store = data_store.get()

    for user in store['users']:
        if user['email'] == email:
            raise InputError("Email has already been registered")

    new_id = create_new_id(store['users'])

    handle_str = ''
    count = 0
    for char in (name_first + name_last).lower():
        if (char.isalpha() or char.isnumeric()) and count < 20:
            handle_str = handle_str + char
            count += 1

    if check_handle_match(handle_str, store['users']):
        i = 0
        modified_str = handle_str
        while check_handle_match(modified_str, store['users']):
            modified_str = handle_str
            modified_str =  modified_str + str(i)
            i += 1
        handle_str = modified_str
        
    password = hashlib.sha256(password.encode()).hexdigest()

    store['users'].append({'u_id': new_id, 'email': email, 'name_first': name_first,
                           'name_last': name_last, 'password': password, 'handle_str': handle_str, 'profile_img_url': f'{config.url}static/0.jpg', 'stats' : {
                    'channels_joined': [{'num_channels_joined': 0, 'time_stamp': 0}],
                    'dms_joined': [{'num_dms_joined': 0, 'time_stamp': 0}],
                    'messages_sent': [{'num_messages_sent': 0, 'time_stamp': 0}],
                    'involvement_rate': 0 
                    }})

    
    if new_id == 1:
        store['global_owners'].append(new_id)

    data_store.set(store)

    return {
        'auth_user_id': new_id
    }

