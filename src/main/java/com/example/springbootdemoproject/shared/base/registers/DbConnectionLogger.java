package com.example.springbootdemoproject.shared.base.registers;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Component
public class DbConnectionLogger {

    private static final Logger logger = LoggerFactory.getLogger(DbConnectionLogger.class);

    private final DataSourceProperties dataSourceProperties;

    public DbConnectionLogger(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @PostConstruct
    public void logConnection() {
        String url = dataSourceProperties.getUrl();
        String username = dataSourceProperties.getUsername();

        // Avoid logging passwords
        logger.info("Database URL: {}, Username: {}", url, username);
    }
}

//ToDo remove me
//ToDo rename app jar to actual name in docker file
