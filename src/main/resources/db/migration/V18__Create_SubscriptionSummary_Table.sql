CREATE TABLE IF NOT EXISTS subscriptionSummary (
    id UUID PRIMARY KEY,
    week INTEGER,
    amount INTEGER,
    createdAt DATE
);