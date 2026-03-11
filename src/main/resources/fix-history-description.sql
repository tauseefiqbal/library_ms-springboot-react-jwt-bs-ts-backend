-- Fix History table description column length
-- This fixes the "Data too long for column 'description'" error when returning books

ALTER TABLE `History` MODIFY COLUMN `description` TEXT;
