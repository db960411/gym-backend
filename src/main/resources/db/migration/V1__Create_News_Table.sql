CREATE TABLE IF NOT EXISTS news (
    id INT PRIMARY KEY,
    body VARCHAR(2048) null,
    title VARCHAR(255) null,
    author VARCHAR(255) null,
    category VARCHAR(255) null,
    imageUrl VARCHAR(255) null,
    createdAt DATE null null
);

INSERT INTO news (id)
SELECT 1
WHERE NOT EXISTS (SELECT 1 FROM news WHERE id = 1);