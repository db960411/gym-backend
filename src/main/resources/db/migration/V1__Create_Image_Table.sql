CREATE TABLE IF NOT EXISTS image (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    data BYTEA
);