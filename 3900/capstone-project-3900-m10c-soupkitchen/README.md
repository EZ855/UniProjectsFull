# capstone-project-3900-m10c-soupkitchen 


# Setup instructions

```bash
# install mysql and java
sudo apt install mysql-server
sudo apt install default-jre

# Set up database
sudo mysql < api/setup.sql  # have to enter root password
mysql -h localhost -u soupkitchen_backend soupkitchen -p < api/database.sql  # enter password 'soupkitchen'
mysql -h localhost -u soupkitchen_backend soupkitchen -p < api/dummy.sql  # enter password 'soupkitchen'

# run
sudo systemctl start mysql
# in VSCode open 'backend' folder, go to SoupKitchenApplication.java click run
```
