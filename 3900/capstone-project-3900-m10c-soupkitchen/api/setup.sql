DROP DATABASE IF EXISTS soupkitchen;
CREATE DATABASE soupkitchen;
DROP  user IF EXISTS 'soupkitchen_backend'@'localhost';
CREATE USER 'soupkitchen_backend'@'localhost' IDENTIFIED BY 'soupkitchen';
GRANT ALL PRIVILEGES ON soupkitchen.* TO 'soupkitchen_backend'@'localhost';
FLUSH PRIVILEGES;
