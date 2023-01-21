echo 'started setup'
sudo mysql < api/setup.sql
sudo mysql soupkitchen < api/database.sql
sudo mysql soupkitchen < api/dummy.sql
python3 setup.py
echo 'finished setup'