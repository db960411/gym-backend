CREATE TABLE IF NOT EXISTS chat (
    id INT PRIMARY KEY NOT NULL,
    sender_id INT NOT NULL,
    sender_status VARCHAR(50) DEFAULT 'text',
    receiver_id INT,
    receiver_status VARCHAR(50) DEFAULT 'text',
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'sent',
    type VARCHAR(50) DEFAULT 'text',

    FOREIGN KEY (sender_id) REFERENCES social(id),
    FOREIGN KEY (receiver_id) REFERENCES social(id)
);

-- Add indexes to the user_id and plan_id columns for faster data retrieval
CREATE INDEX IF NOT EXISTS idx_sender_id ON chat(sender_id);
CREATE INDEX IF NOT EXISTS idx_receiver_id ON chat(receiver_id);
