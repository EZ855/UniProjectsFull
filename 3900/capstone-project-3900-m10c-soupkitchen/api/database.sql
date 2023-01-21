CREATE TABLE blog (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(500) NOT NULL,
  content VARCHAR(5000) NOT NULL
);

CREATE TABLE users (
    id INT NOT NULL auto_increment,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    registration_date date,
    profile_base64 text,
    PRIMARY KEY (id),
    UNIQUE(email)
);

CREATE TABLE passwords (
    id INT NOT NULL auto_increment,
    user INT NOT NULL,
    password_hashed VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user) REFERENCES users(id)
);

CREATE TABLE auth_tokens (
    id INT NOT NULL auto_increment,
    user INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user) REFERENCES users(id)
);

CREATE TABLE user_followers (    
    id INT NOT NULL auto_increment,
    user INT NOT NULL,
    follower INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user) REFERENCES users(id),
    FOREIGN KEY (follower) REFERENCES users(id)
);

CREATE TABLE recipes (
    id INT NOT NULL auto_increment,
    name VARCHAR(256) NOT NULL,
    is_published int, /*if null or greater than 0 then it is published*/
    time DATETIME NOT NULL,
    author INT NOT NULL,  /* the creator */
    meal_type VARCHAR(256), /*breakfast/lunch/dinner/snack*/
    photo text,  /*path to file*/
    PRIMARY KEY (id),
    FOREIGN KEY (author) REFERENCES users(id)
);

CREATE TABLE ingredients (
    id INT NOT NULL auto_increment,
    name VARCHAR(256) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE(name)
);

CREATE TABLE recipe_ingredients (
    id INT NOT NULL auto_increment,
    recipe INT NOT NULL,
    ingredient INT NOT NULL,
    quantity INT NOT NULL,
    units VARCHAR(256),
    PRIMARY KEY (id),
    FOREIGN KEY (recipe) REFERENCES recipes(id),
    FOREIGN KEY (ingredient) REFERENCES ingredients(id)
);

CREATE TABLE equipments (
    id INT NOT NULL auto_increment,
    name VARCHAR(256) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE(name)
);

CREATE TABLE recipe_equipments (
    id INT NOT NULL auto_increment,
    recipe INT NOT NULL,
    equipment INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe) REFERENCES recipes(id),
    FOREIGN KEY (equipment) REFERENCES equipments(id)
);

CREATE TABLE tags (
    id INT NOT NULL auto_increment,
    name VARCHAR(256) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE(name)
);

CREATE TABLE recipe_tags (
    id INT NOT NULL auto_increment,
    recipe INT NOT NULL,
    tag INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe) REFERENCES recipes(id),
    FOREIGN KEY (tag) REFERENCES tags(id)
);

CREATE TABLE recipe_methods (
     id INT NOT NULL auto_increment,
     recipe INT NOT NULL,
     method MEDIUMTEXT NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (recipe) REFERENCES recipes(id)
);

CREATE TABLE comments (
    id INT NOT NULL auto_increment,
    commenter INT NOT NULL,
    recipe INT NOT NULL,
    comment text,  /*optional*/
    PRIMARY KEY (id),
    FOREIGN KEY (commenter) REFERENCES users(id),
    FOREIGN KEY (recipe) REFERENCES recipes(id)
);

CREATE TABLE likes (
    id INT NOT NULL auto_increment,
    user INT NOT NULL,
    liked bool NOT NULL,  /*0 => dislike, 1=> like*/
    recipe INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user) REFERENCES users(id),
    FOREIGN KEY (recipe) REFERENCES recipes(id),
    UNIQUE KEY `single_like` (`user`,`recipe`)
);

CREATE TABLE cookbooks (
    id INT NOT NULL auto_increment,
    name VARCHAR(256) NOT NULL,
    creator INT NOT NULL,
    FOREIGN KEY (creator) REFERENCES users(id),
    PRIMARY KEY (id)
);

CREATE TABLE cookbook_recipes (
    id INT NOT NULL auto_increment,
    cookbook_index INT NOT NULL,
    recipe INT NOT NULL,
    cookbook INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe) REFERENCES recipes(id),
    FOREIGN KEY (cookbook) REFERENCES cookbooks(id)
);

CREATE TABLE cookbook_subscribers (    
    id INT NOT NULL auto_increment,
    cookbook INT NOT NULL,
    follower INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cookbook) REFERENCES cookbooks(id),
    FOREIGN KEY (follower) REFERENCES users(id)
);
