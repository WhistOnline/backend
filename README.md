# backend

SQL Role Creation and Privileges Grant

```SQL 
CREATE ROLE whist LOGIN PASSWORD 'whist';

REVOKE CONNECT ON DATABASE users  FROM PUBLIC;
GRANT CONNECT on DATABASE users  TO whist;

GRANT USAGE ON SCHEMA public TO new_user;

GRANT ALL PRIVILEGES ON DATABASE users TO whist;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO whist;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO whist;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO whist;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO whist;
```

SQL Table Creation Scripts
 
 ```SQL
 CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    user_name varchar(255),
    email varchar(255),
    password varchar(255)
);


CREATE TABLE user_login (
    user_login_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    token varchar(255),
    token_expire_time varchar(255)
);


INSERT INTO users (user_name , email , password)
VALUES ('test', 'test@gmail.com', 'testPassword');
```

```SQL 
CREATE TABLE Player (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    draws INTEGER DEFAULT 0,
    total_games INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW()
);
```

```SQL 
CREATE TABLE GameSession (
    id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    max_players INTEGER NOT NULL,
    current_round INTEGER DEFAULT 0,
    winner_player_id BIGINT REFERENCES Player(id),
    created_at TIMESTAMP DEFAULT NOW()
);
```

```SQL 
CREATE TABLE GameSessionPlayer (
    id SERIAL PRIMARY KEY,
    player_id BIGINT REFERENCES Player(id),
    game_session_id BIGINT REFERENCES GameSession(id),
    score INTEGER DEFAULT 0,
    is_dealer BOOLEAN DEFAULT FALSE
);
```

```SQL 
CREATE TABLE Round (
    id SERIAL PRIMARY KEY,
    game_session_id BIGINT REFERENCES GameSession(id),
    round_number INTEGER NOT NULL,
    trump_suit VARCHAR(50),
    type VARCHAR(50),
    total_tricks_won INTEGER DEFAULT 0
);
```

```SQL 
CREATE TABLE RoundMove (
    id SERIAL PRIMARY KEY,
    round_id BIGINT REFERENCES Round(id),
    player_id BIGINT REFERENCES Player(id),
    card_played VARCHAR(10) NOT NULL,
    move_order INTEGER NOT NULL,
    trick_winner_id BIGINT REFERENCES Player(id)
);
```

```SQL 
CREATE TABLE Bid (
    id SERIAL PRIMARY KEY,
    round_id BIGINT REFERENCES Round(id),
    player_id BIGINT REFERENCES Player(id),
    bid_value INTEGER NOT NULL,
    tricks_won INTEGER DEFAULT 0
);
```

```SQL 
CREATE TABLE Card (
    id SERIAL PRIMARY KEY,
    suit VARCHAR(50) NOT NULL,
    value VARCHAR(50) NOT NULL
);
```

Populate the Card table:

```SQL 
INSERT INTO Card (suit, value) VALUES 
('Hearts', '7'), ('Hearts', '8'), ('Hearts', '9'), ('Hearts', '10'), ('Hearts', 'J'), ('Hearts', 'Q'), ('Hearts', 'K'), ('Hearts', 'A'),
('Diamonds', '7'), ('Diamonds', '8'), ('Diamonds', '9'), ('Diamonds', '10'), ('Diamonds', 'J'), ('Diamonds', 'Q'), ('Diamonds', 'K'), ('Diamonds', 'A'),
('Clubs', '7'), ('Clubs', '8'), ('Clubs', '9'), ('Clubs', '10'), ('Clubs', 'J'), ('Clubs', 'Q'), ('Clubs', 'K'), ('Clubs', 'A'),
('Spades', '7'), ('Spades', '8'), ('Spades', '9'), ('Spades', '10'), ('Spades', 'J'), ('Spades', 'Q'), ('Spades', 'K'), ('Spades', 'A');
```

