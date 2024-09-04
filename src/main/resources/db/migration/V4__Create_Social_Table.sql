CREATE TABLE IF NOT EXISTS social (
    id SERIAL PRIMARY KEY,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES _user (id)
);

-- Add an index for faster queries on "user_id"
CREATE INDEX IF NOT EXISTS idx_user_id ON social("user_id");