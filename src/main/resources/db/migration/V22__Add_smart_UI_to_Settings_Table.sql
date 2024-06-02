DO $$
BEGIN
    -- Check if the column exists in the settings table
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'settings'
        AND column_name = 'smart_ui'
    ) THEN
        -- Add the new column to the settings table
        ALTER TABLE settings
            ADD COLUMN smart_ui boolean NOT NULL DEFAULT FALSE;
    END IF;
END $$;
