CREATE TABLE IF NOT EXISTS items (
    id serial PRIMARY KEY,
    description varchar NOT NULL,
    created timestamp NOT NULL,
    done boolean NOT NULL
);