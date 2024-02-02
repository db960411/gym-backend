-- Create the progress table
CREATE TABLE IF NOT EXISTS notes (
    id UUID PRIMARY KEY,
    user_id INT,
    createdAt DATE,
    title VARCHAR(255) NULL,
    content TEXT,
    category VARCHAR(255) NULL,
    FOREIGN KEY (user_id) REFERENCES _user(id)
);

-- Add an index for faster queries on user_id
CREATE INDEX IF NOT EXISTS idx_user_id ON notes(user_id);
