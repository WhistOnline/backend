# backend

SQL Role Creation and Privileges Grant

```SQL 
CREATE ROLE whist LOGIN PASSWORD 'whist';

REVOKE CONNECT ON DATABASE users  FROM PUBLIC;
GRANT CONNECT on DATABASE users  TO whist;

GRANT USAGE ON SCHEMA public TO whist;

GRANT ALL PRIVILEGES ON DATABASE users TO whist;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO whist;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO whist;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO whist;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO whist;
```

SQL Table Creation Scripts
 
 ```SQL
 CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username varchar(255),
    email varchar(255),
    password varchar(255),
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    draws INTEGER DEFAULT 0,
    total_games INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW()
);


CREATE TABLE user_login (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    token varchar(255),
    token_expire_time varchar(255)
);
```

```SQL 
CREATE TABLE Game_Session (
    id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    max_players INTEGER NOT NULL,
    current_round INTEGER DEFAULT 0,
    winner_player_id BIGINT REFERENCES users(id),
    is_active BOOLEAN,
    game_code VARCHAR(10),
    created_at TIMESTAMP DEFAULT NOW()
);
```

```SQL 
CREATE TABLE Game_Session_Player (
    id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES Users(id),
    game_session_id BIGINT REFERENCES Game_Session(id),
    score INTEGER DEFAULT 0,
    is_turn BOOLEAN DEFAULT FALSE
);
```

```SQL 
CREATE TABLE Round (
    id SERIAL PRIMARY KEY,
    game_session_id BIGINT REFERENCES Game_Session(id),
    round_number INTEGER NOT NULL,
    trump_suit VARCHAR(50),
    type VARCHAR(50)
);
```

```SQL 
CREATE TABLE Card (
    id SERIAL PRIMARY KEY,
    suit VARCHAR(50) NOT NULL,
    value VARCHAR(50) NOT NULL
);
```

```SQL 
CREATE TABLE RoundMove (
    id SERIAL PRIMARY KEY,
    round_id BIGINT REFERENCES Round(id),
    player_id BIGINT REFERENCES Game_Session_Player(id),
    card_played BIGINT REFERENCES Card(id),
    trick_winner_id BIGINT REFERENCES Users(id)
);
```

```SQL 
CREATE TABLE Bid (
    id SERIAL PRIMARY KEY,
    round_id BIGINT REFERENCES Round(id),
    player_id BIGINT REFERENCES Game_Session_Player(id),
    bid_value INTEGER NOT NULL,
    tricks_won INTEGER DEFAULT 0
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

1 1 1 1   2      3           4              5                6                     7                       8          
1 2 3 4 (5 6) (7 8 9) (10 11 12 13) (14 15 16 17 18) (19 20 21 22 23 24) (25 26 27 28 29 30 31) (32 33 34 35 36 37 38 39)    
1 2 3 4   5      6

MAP <RoundNo, List<Integer>>
1 -> List<Integer> = [1, 1, 1]
2 -> [1, 1, 2]
3 -> [1, 1, 3]
4 -> [1, 1, 4]
5 -> [2, 1, 5]
6 -> [1, 0, 5]
7 -> [3, 1, 6]
8 -> [2, 0, 6]
9 -> [1, 0, 6]
10 -> [4, 1, 7]
11 -> [3,                         0,                            7]
       |                          |                             |
no. of Cards in hand    should cards be dealt         actual round number
