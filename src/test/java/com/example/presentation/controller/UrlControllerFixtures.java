package com.example.presentation.controller;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

public final class UrlControllerFixtures {
    public static Response createShortUrl(String url) {
        return
            given().
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body("""
                    {
                        "url": "%s"
                    }
                    """.formatted(url)).
            when().
                post("/v1/url").
            then().
                statusCode(SC_CREATED).
                extract().
                response();
    }


}
