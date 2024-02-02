CREATE TABLE IF NOT EXISTS news_letter (
    id INT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES _user(id)
);
