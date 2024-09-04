CREATE TABLE IF NOT EXISTS exerciseAnalytics (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    modifiedAt TIMESTAMP,
    exercise_type_id UUID,
    initialReps DOUBLE PRECISION DEFAULT 0,
    currentReps DOUBLE PRECISION DEFAULT 0,
    repsIncrease DOUBLE PRECISION DEFAULT 0,
    initialSets DOUBLE PRECISION DEFAULT 0,
    currentSets DOUBLE PRECISION DEFAULT 0,
    setsIncrease DOUBLE PRECISION DEFAULT 0,
    initialWeight DOUBLE PRECISION DEFAULT 0,
    currentWeight DOUBLE PRECISION DEFAULT 0,
    weightIncrease DOUBLE PRECISION DEFAULT 0,
    distance DOUBLE PRECISION DEFAULT 0,
    time DOUBLE PRECISION DEFAULT 0,
    steps DOUBLE PRECISION DEFAULT 0,
    BPM DOUBLE PRECISION DEFAULT 0,
    user_id int,

    FOREIGN KEY (exercise_type_id) REFERENCES exercise_type (id),
    FOREIGN KEY (user_id) REFERENCES _user(id)
);

CREATE INDEX IF NOT EXISTS idx_user_id ON userAnalytics(user_id);
