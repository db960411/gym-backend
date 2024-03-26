CREATE TABLE IF NOT EXISTS userRegistrationsSummary (
    id UUID PRIMARY KEY,
    week INTEGER,
    name VARCHAR(255),
    value INTEGER,
    createdAt DATE
);