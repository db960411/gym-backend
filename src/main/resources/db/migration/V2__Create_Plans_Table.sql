-- Create the 'plans' table if it doesn't exist
CREATE TABLE IF NOT EXISTS plans (
    id INT PRIMARY KEY,
    category VARCHAR(255) DEFAULT 'Strength',
    description VARCHAR(255) DEFAULT 'This is the strength plan',
    name VARCHAR(255) DEFAULT 'Strength',
    subscription_type VARCHAR(255) DEFAULT 'BASIC'
);

-- Insert a record into 'plans' with id=1 if it doesn't already exist
INSERT INTO plans (id, category, description, name, subscription_type)
SELECT 1, 'Strength', 'This is the strength plan', 'strength', 'BASIC'
WHERE NOT EXISTS (SELECT 1 FROM plans WHERE id = 1);

INSERT INTO plans (id, category, description, name, subscription_type)
SELECT 2, 'Mix', 'This is the mix plan', 'mix', 'BASIC'
WHERE NOT EXISTS (SELECT 1 FROM plans WHERE id = 2);

INSERT INTO plans (id, category, description, name, subscription_type)
SELECT 3, 'Lose weight', 'This is the lose weight plan', 'lose-weight', 'BASIC'
WHERE NOT EXISTS (SELECT 1 FROM plans WHERE id = 3);