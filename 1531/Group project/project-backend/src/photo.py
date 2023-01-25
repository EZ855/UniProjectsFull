import urllib.request 
from PIL import Image 
import sys 
from src import config
from src.data_store import data_store
import requests
from src.error import InputError, AccessError


# based on iteration 3 lecture code 

OK = 200

def type_check(ending):
    if ending != 'jpg':
        raise InputError(description="Not jpg")    
        
def photo_download(img_url, u_id, ending):
    urllib.request.urlretrieve(img_url, f'src/static/{u_id}.{ending}')

def dimensions_check(file_path, x1, y1, x2, y2):
    img = Image.open(file_path)
    (x, y) = img.size
    if (x2 > x) or (y2 > y):
        raise InputError(description="Incorrect dimensions")
    if (x1 > x2) or (y1 > y2):
        raise InputError(description="Incorrect dimensions")
 
def photo_crop(file_path, x1, y1, x2, y2):
    imageObject = Image.open(file_path)
    cropped = imageObject.crop((x1, y1, x2, y2))
    cropped.save(file_path)
    
def store_user_photo(u_id, ending):
    store = data_store.get()
    users = store['users']
    channels = store['channels']
    dms = store['dms']
    for user in users:
        if user['u_id'] == u_id:
            break 
    user['profile_img_url'] = f'{config.url}static/{u_id}.{ending}'

    for channel in channels:
        for member in channel['all_members']:
            if member['u_id'] == u_id:
                member['profile_img_url'] = f'{config.url}static/{u_id}.{ending}'           
        for member in channel['owner_members']:
            if member['u_id'] == u_id:
                member['profile_img_url'] = f'{config.url}static/{u_id}.{ending}'
            
    for dm in dms:
        for member in dm['all_members']:
            if member['u_id'] == u_id:
               member['profile_img_url'] = f'{config.url}static/{u_id}.{ending}'              
        for member in dm['owner_members']:
            if member['u_id'] == u_id:
               member['profile_img_url'] = f'{config.url}static/{u_id}.{ending}'  

