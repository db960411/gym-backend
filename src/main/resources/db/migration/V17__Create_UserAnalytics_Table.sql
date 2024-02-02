CREATE TABLE IF NOT EXISTS userAnalytics (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    modifiedAt TIMESTAMP,
    user_id int,
    exerciseType VARCHAR(255),
    initialUserWeight double,
    currentUserWeight double,
    userWeightPercentageIncrease double,
    initialProgressWeight double,
    currentProgressWeight double,
    userProgressWeightPercentageIncrease double,
    initialProgressSets double,
    currentProgressSets double,
    userProgressRepsPercentageIncrease double,
    initialProgressReps double,
    currentProgressReps double,
    userProgressRepsPercentageIncrease double,
    initialProgressSets double,
    progressSets double,
    userProgressSetsPercentageIncrease double,

    FOREIGN KEY (progress_id) REFERENCES progress(id),
    FOREIGN KEY (user_id) REFERENCES _user(id)
);

CREATE INDEX IF NOT EXISTS idx_user_id ON userAnalytics(user_id);

