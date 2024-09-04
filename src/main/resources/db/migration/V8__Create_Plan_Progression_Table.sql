-- Create the 'plan_progression' table
CREATE TABLE IF NOT EXISTS plan_progression (
    id INT PRIMARY KEY NOT NULL,
    user_id INT NOT NULL,
    plan_id INT NOT NULL,
    day INT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES _user(id),
    FOREIGN KEY (plan_id) REFERENCES plans(id)
);

-- Add indexes to the user_id and plan_id columns for faster data retrieval
CREATE INDEX IF NOT EXISTS idx_user_id ON plan_progression(user_id);
CREATE INDEX IF NOT EXISTS idx_plan_id ON plan_progression(plan_id);