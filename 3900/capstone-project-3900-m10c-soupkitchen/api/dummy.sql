INSERT INTO users (id, username, email) VALUES
    (1, 'SpicyEats', 'ilovetofu@example.com'),
    (2, 'IceCreamGirl', 'frozen.cream@example.com'),
    (3, 'Hunter07', 'meat4ever@example.com'),
    (4, 'CupcakesUwU', 'chocochipcuppie@example.com'),
    (5, 'JRRT', 'jrrt@example.com'),
    (6, 'VeganChips', 'vegan98@example.com')
;

INSERT INTO passwords (user, password_hashed) VALUES
    (1, 'qwertyQWERT123!@#'),
    (2, 'qwertyQWERT123!@#'),
    (3, 'qwertyQWERT123!@#'),
    (4, 'qwertyQWERT123!@#'),
    (5, 'qwertyQWERT123!@#'),
    (6, 'qwertyQWERT123!@#')
;

INSERT INTO auth_tokens (id, user, token) VALUES
    (1, 1, "dummy_token_789"),
    (2, 2, "what")
;

