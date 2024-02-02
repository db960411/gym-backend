CREATE TABLE IF NOT EXISTS friendship_request (
    id SERIAL PRIMARY KEY,
    sender_social_id INT,
    receiver_social_id INT,
    status VARCHAR(255),

    FOREIGN KEY (sender_social_id) REFERENCES social (id),
    FOREIGN KEY (receiver_social_id) REFERENCES social (id)
);

CREATE INDEX IF NOT EXISTS idx_sender_social_id ON friendship_request(sender_social_id);
CREATE INDEX IF NOT EXISTS idx_receiver_social_id ON friendship_request(receiver_social_id);
