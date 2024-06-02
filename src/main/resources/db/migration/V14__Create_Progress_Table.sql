-- Create the progress table
CREATE TABLE IF NOT EXISTS progress (
    id UUID PRIMARY KEY,
    profile_id INT,
    exercise_type_id UUID null,
    sets DOUBLE PRECISION,
    reps DOUBLE PRECISION,
    weight DOUBLE PRECISION,
    distance DOUBLE PRECISION,
    steps DOUBLE PRECISION,
    heartRate DOUBLE PRECISION,
    time DOUBLE PRECISION,

    FOREIGN KEY (profile_id) REFERENCES profile (id),
    FOREIGN KEY (exercise_type_id) REFERENCES exercise_type (id)
);

-- Add an index for faster queries on profile_id
CREATE INDEX IF NOT EXISTS idx_profile_id ON progress(profile_id);
