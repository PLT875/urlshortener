package com.example.testingutil;

import com.example.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, PostgreSQLContainerExtension.class})
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Value("${server.port}")
    protected String serverPort;

    @BeforeEach
    void setup() {
        RestAssured.port = Integer.parseInt(serverPort);
    }
}
