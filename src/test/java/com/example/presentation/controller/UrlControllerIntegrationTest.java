package com.example.presentation.controller;

import com.example.testingutil.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UrlControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void createShortUrlAndIsIdempotent() {
        String requestBody = """
                {
                    "url": "https://google.com"
                }
                """;

        Response response = given().
            header("Content-Type", "application/json").
            header("Accept", "application/json").
            body(requestBody).
            when().
                post("/v1/url").
            then().
                extract().
                response();

        assertThat(response.statusCode()).isEqualTo(SC_CREATED);

        String key = response.getBody().jsonPath().getString("key");
        String longUrl = response.getBody().jsonPath().getString("long_url");
        String shortUrl = response.getBody().jsonPath().getString("short_url");
        assertThat(shortUrl).startsWith("http://localhost:8888");

        given().
            header("Content-Type", "application/json").
            header("Accept", "application/json").
            body(requestBody).
        when().
            post("/v1/url").
        then().
            statusCode(SC_CREATED).
            body("key", equalTo(key)).
            body("long_url", equalTo(longUrl)).
            body("short_url", equalTo(shortUrl));
    }

    @Test
    void createShortUrlBadRequest() {
        given().
            header("Content-Type", "application/json").
            header("Accept", "application/json").
            body("{}").
        when().
            post("/v1/url").
        then().
            statusCode(SC_BAD_REQUEST);
    }

    @Test
    void deleteShortenUrl() {
        String requestBody = """
                {
                    "url": "https://www.wikipedia.org"
                }
                """;

        Response response = given().
            header("Content-Type", "application/json").
            header("Accept", "application/json").
            body(requestBody).
            when().
                post("/v1/url").
            then().
                extract().
                response();

        assertThat(response.statusCode()).isEqualTo(SC_CREATED);
        String key = response.getBody().jsonPath().getString("key");

        when().
            delete("/v1/url/" + key).
        then().
            statusCode(SC_NO_CONTENT);

        when().
            delete("/v1/url/" + key).
        then().
            statusCode(SC_NOT_FOUND);
    }

}
