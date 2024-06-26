CREATE TABLE IF NOT EXISTS profile (
    id INT PRIMARY KEY,
    user_id INT NOT NULL,
    display_name VARCHAR(255) NULL,
    height DOUBLE PRECISION NULL,
    weight DOUBLE PRECISION NULL,
    language VARCHAR(255) NULL,
    nationality VARCHAR(255) NULL,
    gender VARCHAR(255) NULL,
    date_of_birth VARCHAR(255) NULL,
    fitness_goals VARCHAR(255) NULL,
    FOREIGN KEY (user_id) REFERENCES _user(id)
);
CREATE INDEX IF NOT EXISTS idx_user_id ON social("user_id");
