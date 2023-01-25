import pytest

from src.auth import auth_register_v1
from src.auth import auth_login_v1
from src.error import InputError
from src.other import clear_v1

def test_register_invalid_email():
    clear_v1()
    with pytest.raises(InputError):
        auth_register_v1('', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('email', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@someplace', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@someplace', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@someplace.', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@someplace.a', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('@', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('@someplace', 'password', 'monty', 'python')

def test_register_invalid_password():
    clear_v1()
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', '', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', 'four', 'monty', 'python')
        
def test_register_invalid_name_first():
    clear_v1()
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', 'password', '', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', 'a' * 51, '', 'python')

def test_register_invalid_name_last():
    clear_v1()
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', 'password', 'monty', '')
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', 'monty', '', 'b' * 51)
        
def test_no_duplicates():
    clear_v1()
    auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    
def test_login_invalid_email():
    clear_v1()
    auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    with pytest.raises(InputError):
        auth_login_v1('nouser@mail.nouser', 'password')
        
def test_login_invalid_password():
    clear_v1()
    auth_register_v1('monty@mail.valid', 'rightpassword', 'monty', 'python')
    with pytest.raises(InputError):
        auth_login_v1('monty@mail.valid', 'wrongpassword')

def test_login_functionality():
    clear_v1()
    auth_register_v1('monty@mail.valid', 'password', 'monty', 'python')
    auth_login_v1('monty@mail.valid', 'password')
