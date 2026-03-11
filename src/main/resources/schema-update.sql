-- Update img column length in history table to support base64 images
ALTER TABLE history MODIFY COLUMN img LONGTEXT;

-- Update img column length in book table to support base64 images
ALTER TABLE book MODIFY COLUMN img LONGTEXT;
