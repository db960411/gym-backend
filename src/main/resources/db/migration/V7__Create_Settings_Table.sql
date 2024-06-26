CREATE TABLE IF NOT EXISTS settings (
    id INT PRIMARY KEY,
    user_id INT NOT NULL,
    subscription_id INT,
    profile_id INT,
    receive_emails boolean NOT NULL,
    allow_notifications boolean NOT NULL DEFAULT TRUE,
    smart_ui boolean NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES _user(id),
    FOREIGN KEY (subscription_id) REFERENCES subscription(id),
    FOREIGN KEY (profile_id) REFERENCES profile(id)
);
CREATE INDEX IF NOT EXISTS idx_user_id ON social("user_id");