-- Fix History table description column size issue
-- This command changes the description column from VARCHAR(255) to TEXT
-- to allow longer book descriptions in the history records

USE md8id2hgb8635iss;

ALTER TABLE history MODIFY COLUMN description TEXT;

-- Verify the change
DESCRIBE history;
