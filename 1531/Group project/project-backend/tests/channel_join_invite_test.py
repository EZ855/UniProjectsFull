import pytest
from src.auth import auth_register_v1
from src.channels import channels_create_v1 
from src.channel import channel_join_v1
from src.channel import channel_invite_v1
from src.channel import channel_details_v1
from src.error import InputError
from src.error import AccessError
from src.other import clear_v1
def test_channel_join_invalid_channel():
    clear_v1()   
    user1 = auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2 = auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    with pytest.raises(InputError):
        channel_join_v1(user1['auth_user_id'],100)    
def test_channel_join_duplicate_user():
    clear_v1()   
    user1 = auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2 = auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channel1 = channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)	
    with pytest.raises(InputError):
        channel_join_v1(user1['auth_user_id'],channel1['channel_id'])       
def test_channel_join_accesserror():
    clear_v1()   
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    user4=auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channel2=channels_create_v1(user2['auth_user_id'],'League of legends',False)
    with pytest.raises(AccessError):
        channel_join_v1(user4['auth_user_id'],channel2['channel_id'])
def test_invite_already_member():
    clear_v1()   
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channel1=channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    with pytest.raises(InputError):
        channel_invite_v1(user1['auth_user_id'],channel1['channel_id'],user1['auth_user_id'])
def test_invite_invalid_channel_id():
    clear_v1()   
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    user4=auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    with pytest.raises(InputError):
        channel_invite_v1(user1['auth_user_id'],100,user4['auth_user_id'])
def test_invite_invalid_user_id():
    clear_v1()   
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channel1=channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    with pytest.raises(InputError):
    	channel_invite_v1(user1['auth_user_id'],channel1['channel_id'],100)
def test_invite_no_permission():
    clear_v1()   
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    user3=auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channel1=channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    with pytest.raises(AccessError):
    	channel_invite_v1(user2['auth_user_id'],channel1['channel_id'],user3['auth_user_id'])

def test_invite_successfully():
    clear_v1()
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    user3=auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channel1=channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    channel_invite_v1(user1['auth_user_id'],channel1['channel_id'],user3['auth_user_id'])
    allmembers = channel_details_v1(user1['auth_user_id'],channel1['channel_id'])
    assert(len(allmembers['all_members'])==2)
def test_join_successfully():
    clear_v1()
    user1=auth_register_v1('mark@abc.com','nqn123456','Mark','Fish')
    user2=auth_register_v1('abcd@mc.com','allgood123','Michael','Joedan')
    auth_register_v1('banana123@qq.com','banan321','Join','Hao')
    user4=auth_register_v1('10223344@qq.com','lal112233','Hi','Hello')
    channel1=channels_create_v1(user1['auth_user_id'],'Fishingclub',True)
    channels_create_v1(user2['auth_user_id'],'League of legends',False)
    channel_join_v1(user4['auth_user_id'],channel1['channel_id'])
    allmembers = channel_details_v1(user1['auth_user_id'],channel1['channel_id'])
    assert(len(allmembers['all_members'])==2)

