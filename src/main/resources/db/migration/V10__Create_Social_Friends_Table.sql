CREATE TABLE IF NOT EXISTS social_friends (
    id SERIAL PRIMARY KEY,
    social_id INT NULL,
    friend_social_id INT NULL,
    FOREIGN KEY (social_id) REFERENCES social (id),
    FOREIGN KEY (friend_social_id) REFERENCES social (id)
);

-- Add an index for faster queries on plan_id
CREATE INDEX IF NOT EXISTS idx_social_id ON social_friends(social_id);
CREATE INDEX IF NOT EXISTS idx_friend_social_id ON social_friends(friend_social_id);
