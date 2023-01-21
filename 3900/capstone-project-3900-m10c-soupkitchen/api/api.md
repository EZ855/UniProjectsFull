# SoupKitchen API documentation


# 1. Auth API

|Path|Description|HTTP method|Request type |Return type|
|----|-----------|-----------|-------------|-----------|
|/auth/register|Register user|POST|`{email, username, password}`|`{success, token, reason (optional)}`|
|/auth/login|Login user|POST|`{username, password}`|`{success, token, reason (optional)}`|
|/auth/forgot|Forgot password|POST|`{email}`|`{success}`|
|/auth/logout|Logout user|POST|`{token}`|`{success}`|


|Field|Data type|
|-----|---------|
|`user_id`|`int`|
|`email`|`string`|
|`username`|`string`|
|`password`|`string`|
|`token`|`string`|
|`success`|`boolean`|
|`reason`|`[string]`|

`reason` is a list of all parameters that are incorrect


# 2. Users API

|Path|Description|HTTP method|Request type |Return type|
|----|-----------|-----------|-------------|-----------|
|/users/info|Get info about user|POST|`{token (optional), user_ids}`|`{users*}`|
|/users/edit|Edit user info|POST|`{token, user*}`|`{success}`|
|/users/follow|Follow a user|POST|`{token, user_id}`|`{success}`|
|/users/unfollow|Unfollow a user|POST|`{token, user_id}`|`{success}`|

|Field|Data type|
|-----|---------|
|`user_ids`|`[user_id]`|
|`user_id`|`int`|
|`users*`|`[user*]`|
|`recipe_id`|`int`|
|`cookbook_id`|`int`|
|`token`|`string`|
|`success`|`boolean`|

## user*

|Field|Data type|
|-----|---------|
|`id`|`int`|
|`username`|`string`|
|`email`|`string`|
|`password_hashed`|`string`|
|`recipe_ids`|`[recipe_id]`|
|`cookbook_ids`|`[cookbook_id]`|
|`follower_ids`|`[user_id]`|
|`following_ids`|`[user_id]`|

### Extra fields

|Field|Data type|
|-----|---------|
|`subscribed_ids`|`[cookbook_id]`|


# 3. Recipes API

|Path|Description|HTTP method|Request type |Return type|
|----|-----------|-----------|-------------|-----------|
|/recipes/info|Get recipe info|POST|`{token (optional), recipe_ids}`|`{recipes*}`|
|/recipes/add|Add new recipe|POST|`{token, recipe*}`|`{success, recipe_id}`|
|/recipes/edit|Edit recipe|POST|`{token, recipe*}`|`{success}`|
|/recipes/delete|Delete recipe|POST|`{token, recipe_id}`|`{success}`|
|/recipes/like|Like recipe|POST|`{token, recipe_id, liked}`|`{success}`|
|/recipes/comments/get|Get comments|GET|`token (optional), recipe_id`|`{comments*}`|
|/recipes/comments/add|Add comment|POST|`{token, comment*}`|`{success}`|
|/recipes/search|Search through recipes|POST|`{token,search*}`|`{recipe_ids}`|
|/recipes/similar|Get a feed of recipes similar to a recipe|GET|`token (optional), recipe_id`|`{recipe_ids}`|
|/recipes/feed|Get a feed of recipes specific to the user|GET|`token (optional)`|`{recipe_ids}`|

|Field|Data type|
|-----|---------|
|`user_id`|`int`|
|`recipe_ids`|`[recipe_id]`|
|`recipe_id`|`int`|
|`comment_id`|`int`|
|`recipes*`|`[recipe*]`|
|`url`|`string`|
|`liked`|`boolean`|
|`token`|`string`|
|`success`|`boolean`|

## search*

|Field|Data type|
|-----|---------|
|`name`|`string`|
|`meal_type`|`breakfast/lunch/dinner/snack`|
|`tags`|`[tag]`|
|`ingredients`|`[string]`|
|`method`|`string`|

## recipe*

|Field|Data type|
|-----|---------|
|`id`|`int`|
|`name`|`string`|
|`meal_type`|`breakfast/lunch/dinner/snack`|
|`tags`|`[string]`|
|`equipments`|`[string]`|
|`ingredients`|`[ingredient*]`|
|`method`|`string`|
|`photo`|`url`|
|`likes`|`[user_id]`|
|`author_id`|`user_id`|

### Extra fields

|Field|Data type|
|-----|---------|
|`liked`|`boolean`|
|`author_username`|`string`|

## ingredient*

|Field|Data type|
|-----|---------|
|`name`|`string`|
|`quantity`|`int`|
|`units`|`string`|

## comment*

|Field|Data type|
|-----|---------|
|`id`|`int`|
|`author_id`|`user_id`|
|`recipe`|`recipe_id`|
|`comment`|`string`|

### Extra fields

|Field|Data type|
|-----|---------|
|`author_username`|`string`|


# 4. Cookbooks API

|Path|Description|HTTP method|Request type |Return type|
|----|-----------|-----------|-------------|-----------|
|/cookbooks/info|Get cookbook info|POST|`{token (optional), cookbook_ids}`|`{cookbooks*}`|
|/cookbooks/create|Create new cookbook|POST|`{token, cookbook*}`|`{success, cookbook_id}`|
|/cookbooks/edit|Edit cookbook|POST|`{token, cookbook*}`|`{success}`|
|/cookbooks/delete|Delete cookbook|POST|`{token, cookbook_id}`|`{success}`|
|/cookbooks/subscribe|Subscribe to a cookbook|POST|`{token, cookbook_id}`|`{success}`|
|/cookbooks/unsubscribe|Unsubscribe from a cookbook|POST|`{token, cookbook_id}`|`{success}`|
|/cookbooks/search|Search through cookbooks|POST|`{token, search*}`|`{cookbook_ids}`|

|Field|Data type|
|-----|---------|
|`cookbook_ids`|`[cookbook_id]`|
|`cookbook_id`|`int`|
|`cookbooks`|`[cookbook*]`|
|`user_id`|`int`|
|`recipe_id`|`int`|
|`token`|`string`|
|`success`|`boolean`|

## cookbook*

|Field|Data type|
|-----|---------|
|`id`|`int`|
|`name`|`string`|
|`creator`|`user_id`|
|`recipe_ids`|`[recipe_ids]`|
|`subscribers`|`[user_id]`|

### Extra fields

|Field|Data type|
|-----|---------|
|`subscribed`|`boolean`|
|`creator_username`|`string`|

## search*

|Field|Data type|
|-----|---------|
|`name`|`string`|

