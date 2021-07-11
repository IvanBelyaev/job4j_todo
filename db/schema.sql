CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    name varchar NOT NULL UNIQUE,
    email varchar NOT NULL UNIQUE,
    password varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id serial PRIMARY KEY,
    description varchar NOT NULL,
    created timestamp NOT NULL,
    done boolean NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id)
);