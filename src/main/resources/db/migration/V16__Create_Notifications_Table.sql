CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY,
    social_id INT,
    from_social_id INT,
    createdAt TIMESTAMP NOT NULL DEFAULT NOW(),
    title VARCHAR(255),
    text VARCHAR(255) NULL,
    category VARCHAR(255) NULL,
    seen BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (social_id) REFERENCES social(id),
    FOREIGN KEY (from_social_id) REFERENCES social(id)
);

CREATE INDEX IF NOT EXISTS idx_social_id ON notifications(social_id);
CREATE INDEX IF NOT EXISTS idx_from_social_id ON notifications(from_social_id);
