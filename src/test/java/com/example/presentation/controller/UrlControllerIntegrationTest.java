package com.example.presentation.controller;

import com.example.persistence.UrlRepository;
import com.example.persistence.entity.UrlEntity;
import com.example.testingutil.BaseIntegrationTest;
import io.restassured.response.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.example.presentation.controller.UrlControllerFixtures.createShortUrl;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UrlControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void whenCreateShortUrl_thenCreated_AndIsIdempotent() {
        String requestBody = """
                {
                    "url": "https://google.com"
                }
                """;

        Response createShortUrlResponse =
            given().
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(requestBody).
            when().
                post("/v1/url").
            then().
                statusCode(SC_CREATED).
                extract().
                response();

        String key = createShortUrlResponse.getBody().jsonPath().getString("key");
        String longUrl = createShortUrlResponse.getBody().jsonPath().getString("long_url");
        String shortUrl = createShortUrlResponse.getBody().jsonPath().getString("short_url");
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
    void whenCreateShortUrl_withCollisionHandling_thenCreated() {
        String url = "http://www.amazon.com";
        String conflictedId1 = DigestUtils.sha256Hex(url).substring(0, 7);
        String conflictedId2 = DigestUtils.sha256Hex(url + 1).substring(0, 7);
        Set<UrlEntity> conflictedUrlEntities = Set.of(
            new UrlEntity(conflictedId1, "http://www.amazon.co.uk"),
            new UrlEntity(conflictedId2, "http://www.amazon.de")
        );
        urlRepository.saveAll(conflictedUrlEntities);

        Response createShortUrlResponse = createShortUrl(url);
        String key = createShortUrlResponse.getBody().jsonPath().getString("key");
        String longUrl = createShortUrlResponse.getBody().jsonPath().getString("long_url");
        String shortUrl = createShortUrlResponse.getBody().jsonPath().getString("short_url");
        assertAll(
            () -> assertThat(key).isNotEqualTo(conflictedId1),
            () -> assertThat(key).isNotEqualTo(conflictedId2),
            () -> assertThat(longUrl).isEqualTo(url),
            () -> assertThat(shortUrl).isEqualTo("http://localhost:8888/" + key));
    }

    @Test
    void whenGetUrl_thenOk() {
        String url = "https://bbc.co.uk";
        Response createShortUrlResponse = createShortUrl(url);
        String key = createShortUrlResponse.getBody().jsonPath().getString("key");
        String longUrl = createShortUrlResponse.getBody().jsonPath().getString("long_url");
        String shortUrl = createShortUrlResponse.getBody().jsonPath().getString("short_url");

        when().
            get("/v1/url/" + key).
        then().
            statusCode(SC_OK).
            body("key", equalTo(key)).
            body("short_url", equalTo(shortUrl)).
            body("long_url", equalTo(longUrl));
    }

    @Test
    void whenGetUrlNotExist_thenNotFound() {
        when().
            get("/v1/url/unknown").
        then().
            statusCode(SC_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "www.google.com",
            "ftp://foo.bar.com",
            "http://localhost:8888/test",
            "https://codingchallenges.fyi/challenges//challenge-url-shortener"
    })
    void whenCreateInvalidShortUrl_thenBadRequest(String badUrl) {
        given().
            header("Content-Type", "application/json").
            header("Accept", "application/json").
            body("""
                    {
                        "url": "%s"
                    }
                """.formatted(badUrl)).
        when().
            post("/v1/url").
        then().
            statusCode(SC_BAD_REQUEST).
            body("message", equalTo("request body contains errors")).
            body("errors[0].message", equalTo("url is invalid")).
            body("errors[0].field", equalTo("url")).
            body("errors[0].value", equalTo(badUrl));
    }

    @Test
    void whenDeleteShortUrl_thenNoContent() {
        String url = "https://www.wikipedia.org";
        Response createShortUrlResponse = createShortUrl(url);
        String key = createShortUrlResponse.getBody().jsonPath().getString("key");

        when().
            delete("/v1/url/" + key).
        then().
            statusCode(SC_NO_CONTENT);
    }

    @Test
    void whenDeleteShortUrlNotExist_thenNotFound() {
        when().
            delete("/v1/url/unknown").
        then().
            statusCode(SC_NOT_FOUND);
    }

}
