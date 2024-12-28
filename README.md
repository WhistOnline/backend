# backend

 Create in postgresql the database: "users"
 Run the following sql commands to create the prerequisites.
 
 
 ```Sql
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


Now use the Grant Wizard to grant db access to your db-user, or just run the following scripts directly:

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
