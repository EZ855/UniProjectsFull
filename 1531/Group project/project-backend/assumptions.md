#Assumptions for channels_create_v1:
*is_public is either True or False (True for public, False for private)
*auth_user_id input is valid (since the user is already logged in when creating a channel)
*name is a string

#Assumptions for channels_list_v1:
*auth_user_id is not solely in owner_members