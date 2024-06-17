CREATE TABLE IF NOT EXISTS userAnalytics (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    modifiedAt TIMESTAMP,
    user_id int,
    initialUserWeight DOUBLE PRECISION DEFAULT 0,
    currentWeight DOUBLE PRECISION DEFAULT 0,
    weightPercentageIncrease DOUBLE PRECISION NULL DEFAULT 0,
    currentBodyFatPercentage DOUBLE PRECISION NULL DEFAULT 0,
    initialBodyFatPercentage DOUBLE PRECISION NULL DEFAULT 0,
    bodyFatPercentageIncrease DOUBLE PRECISION NULL DEFAULT 0,
    initialBMI DOUBLE PRECISION NULL DEFAULT 0,
    currentBMI DOUBLE PRECISION NULL DEFAULT 0,
    BMIPercentageIncrease DOUBLE PRECISION NULL DEFAULT 0,
    workoutDaysDone DOUBLE PRECISION NULL DEFAULT 0,
    initialLongestWorkout DOUBLE PRECISION NULL DEFAULT 0,
    currentLongestWorkout DOUBLE PRECISION NULL DEFAULT 0,
    initialSlowWaveSleep DOUBLE PRECISION NULL DEFAULT 0,
    currentSlowWaveSleep DOUBLE PRECISION NULL DEFAULT 0,
    currentSlowWaveSleep DOUBLE PRECISION DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES _user(id)
);

CREATE INDEX IF NOT EXISTS idx_user_id ON userAnalytics(user_id);