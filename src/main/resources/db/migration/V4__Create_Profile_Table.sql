CREATE TABLE IF NOT EXISTS profile (
    id INT PRIMARY KEY,
    user_id INT NOT NULL,
    display_name VARCHAR(255) NULL,
    height VARCHAR(255) NULL,
    weight VARCHAR(255) NULL,
    language VARCHAR(255) NULL,
    nationality VARCHAR(255) NULL,
    gender VARCHAR(255) NULL,
    date_of_birth VARCHAR(255) NULL,
    fitness_goals VARCHAR(255) NULL,
    FOREIGN KEY (user_id) REFERENCES _user(id)
);
