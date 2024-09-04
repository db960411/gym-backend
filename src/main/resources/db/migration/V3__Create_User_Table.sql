CREATE TABLE IF NOT EXISTS _user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    level VARCHAR(255) NULL,
    role VARCHAR(255) NULL,
    createdAt TIMESTAMP NULL,
    image_id INT,
    FOREIGN KEY (image_id) REFERENCES image (id)
);

CREATE INDEX IF NOT EXISTS idx_image_id ON _user(image_id);
