import requests
import json

my_url = 'http://localhost:8080/recipes/addAll'
my_file = open('api/dummy.json', 'r')
my_object = json.load(my_file)
response = requests.post(my_url, json = my_object)
# print(response)