CREATE TABLE IF NOT EXISTS userAnalytics (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    modifiedAt TIMESTAMP,
    user_id int,
    initialUserWeight DOUBLE PRECISION,
    currentWeight DOUBLE PRECISION,
    weightPercentageIncrease DOUBLE PRECISION NULL,
    currentBodyFatPercentage DOUBLE PRECISION NULL,
    initialBodyFatPercentage DOUBLE PRECISION NULL,
    bodyFatPercentageIncrease DOUBLE PRECISION NULL,
    initialBMI DOUBLE PRECISION NULL DEFAULT 0,
    currentBMI DOUBLE PRECISION NULL DEFAULT 0,
    workoutDaysDone DOUBLE PRECISION NULL DEFAULT 0,
    initialLongestWorkout DOUBLE PRECISION NULL DEFAULT 0,
    currentLongestWorkout DOUBLE PRECISION NULL DEFAULT 0,
    initialSlowWaveSleep DOUBLE PRECISION NULL DEFAULT 0,
    currentSlowWaveSleep DOUBLE PRECISION NULL DEFAULT 0,
    currentSlowWaveSleep DOUBLE PRECISION DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES _user(id)
);

CREATE INDEX IF NOT EXISTS idx_user_id ON userAnalytics(user_id);