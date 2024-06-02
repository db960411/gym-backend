CREATE TABLE IF NOT EXISTS exerciseAnalytics (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    modifiedAt TIMESTAMP,
    exercise_type_id UUID null,
    exerciseTypeName VARCHAR(255) null,
    initialReps DOUBLE PRECISION null,
    currentReps DOUBLE PRECISION null,
    repsPercentageIncrease DOUBLE PRECISION null,
    initialSets DOUBLE PRECISION null,
    currentSets DOUBLE PRECISION null,
    setsPercentageIncrease DOUBLE PRECISION null,
    initialWeight DOUBLE PRECISION null,
    currentWeight DOUBLE PRECISION null,
    weightPercentageIncrease DOUBLE PRECISION null,
    distance DOUBLE PRECISION null,
    time DOUBLE PRECISION null,
    user_id int,

    FOREIGN KEY (exercise_type_id) REFERENCES exercise_type (id),
    FOREIGN KEY (user_id) REFERENCES _user(id)
);

CREATE INDEX IF NOT EXISTS idx_user_id ON userAnalytics(user_id);
