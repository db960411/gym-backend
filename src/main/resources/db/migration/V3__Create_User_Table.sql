CREATE TABLE IF NOT EXISTS _user (
    id INT NOT NULL CONSTRAINT _user_pkey PRIMARY KEY,
    email VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    level VARCHAR(255) NULL,
    role VARCHAR(255) NULL,
    profileImageUrl VARCHAR(255) NULL,
    createdAt DATE
);

