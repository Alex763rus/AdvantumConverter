package com.example.advantumconverter.config;

import org.testcontainers.containers.PostgreSQLContainer;

public final class DatabaseTestConfig {

    private static final String IMAGE = "postgres:15-alpine";

    private static PostgreSQLContainer<?> container;

    private DatabaseTestConfig() {
    }

    public static synchronized void start() {
        if (container == null) {
            PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(IMAGE)
                    .withDatabaseName("advantum")
                    .withUsername("test")
                    .withPassword("test");
            postgres.start();
            container = postgres;
        }
    }

    public static String getJdbcUrl() {
        return container.getJdbcUrl();
    }

    public static String getUsername() {
        return container.getUsername();
    }

    public static String getPassword() {
        return container.getPassword();
    }
}
