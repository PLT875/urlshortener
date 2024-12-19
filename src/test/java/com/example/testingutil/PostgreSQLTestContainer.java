package com.example.testingutil;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLTestContainer extends PostgreSQLContainer<PostgreSQLTestContainer> {
    private static final String DOCKER_IMAGE_NAME = "postgres:14.15-alpine3.21";
    private static PostgreSQLTestContainer container;

    private PostgreSQLTestContainer() {
        super(DOCKER_IMAGE_NAME);
    }

    public static PostgreSQLTestContainer getInstance() {
        if (container == null) {
            container = new PostgreSQLTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_NAME", container.getDatabaseName());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        // JVM handles the shutdown
    }
}
