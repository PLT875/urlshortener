package com.example.presentation.controller;

import com.example.testingutil.BaseIntegrationTest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.example.presentation.controller.UrlControllerFixtures.createShortUrl;
import static io.restassured.RestAssured.given;

import static org.apache.http.HttpHeaders.LOCATION;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

public class RedirectControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void whenShortUrlRequested_thenMovedTemporarily() {
        String longUrl = "https://codingchallenges.fyi/challenges/challenge-url-shortener";
        Response createShortUrlResponse = createShortUrl(longUrl);

        assertThat(createShortUrlResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        String key = createShortUrlResponse.getBody().jsonPath().getString("key");

        given().
            redirects().follow(false).
        when().
            get("/" + key).
        then().
            statusCode(SC_MOVED_TEMPORARILY).
            header(LOCATION, longUrl);
    }

    @Test
    void whenShortUrlRequestedDoesNotExist_thenNotFound() {
        given().
            redirects().follow(false).
        when().
            get("/unknown").
        then().
            statusCode(SC_NOT_FOUND);
    }
}
