package com.example.testingutil;

import com.example.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    static final String POSTGRES_DOCKER_IMAGE = "postgres:14.15-alpine3.21";
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_DOCKER_IMAGE));

    static {
        postgresContainer.start();
    }

    @Value("${server.port}")
    protected String serverPort;

    @BeforeEach
    void setup() {
        RestAssured.port = Integer.parseInt(serverPort);
    }

    @BeforeAll
    static void beforeAll() {
        System.setProperty("DB_URL", postgresContainer.getJdbcUrl());
        System.setProperty("DB_NAME", postgresContainer.getDatabaseName());
        System.setProperty("DB_USERNAME", postgresContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgresContainer.getPassword());
    }
}
