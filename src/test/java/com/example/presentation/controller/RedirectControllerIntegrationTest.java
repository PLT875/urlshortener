package com.example.presentation.controller;

import com.example.testingutil.BaseIntegrationTest;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RedirectControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void redirect() {
        String testUrl = "https://codingchallenges.fyi/challenges/challenge-url-shortener";
        Response createShortUrlResponse = given().
            header("Content-Type", "application/json").
            header("Accept", "application/json").
            body( """
                {
                    "url": "%s"
                }
                """.formatted(testUrl)).
            when().
            post("/v1/url").
            then()
                .extract()
                .response();

        assertThat(createShortUrlResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        String key = createShortUrlResponse.getBody().jsonPath().getString("key");

        Response redirectResponse1 = given().
            redirects().follow(false).
            when().
                get("/" + key).
            then().
                extract().
                response();

        assertAll(
            () -> assertThat(redirectResponse1.statusCode()).isEqualTo(HttpStatus.SC_MOVED_TEMPORARILY),
            () -> assertThat(redirectResponse1.getHeader(HttpHeaders.LOCATION)).isEqualTo(testUrl)
        );

        when().
            delete("/v1/url/" + key).
        then().
            statusCode(HttpStatus.SC_NO_CONTENT);

        given().
            redirects().follow(false).
        when().
            get("/" + key).
        then().
            statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
