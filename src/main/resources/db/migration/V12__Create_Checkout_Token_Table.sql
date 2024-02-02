-- Create the checkout_token table
CREATE TABLE IF NOT EXISTS checkout_token (
    id UUID PRIMARY KEY,
    user_id INT,
    token INT,
    created_at TIMESTAMP,
    expires_at TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES _user (id)
);

-- Add an index for faster queries on user_id
CREATE INDEX IF NOT EXISTS idx_user_id ON checkout_token(user_id);
