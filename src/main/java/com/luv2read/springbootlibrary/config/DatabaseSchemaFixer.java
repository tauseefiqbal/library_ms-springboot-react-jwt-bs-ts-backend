package com.luv2read.springbootlibrary.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSchemaFixer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaFixer.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSchemaFixer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fixHistoryTableSchema() {
        try {
            jdbcTemplate.execute("ALTER TABLE `History` MODIFY COLUMN `description` TEXT");
            jdbcTemplate.execute("ALTER TABLE `History` MODIFY COLUMN `img` LONGTEXT");
            log.info("History table schema verified/updated successfully");
        } catch (Exception e) {
            log.warn("Could not alter History table columns (may already be correct): {}", e.getMessage());
        }
    }
}
