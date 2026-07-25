package com.example.domain.model;

import jakarta.validation.constraints.NotNull;

public record Url(
        @NotNull String id,
        @NotNull String longUrl,
        String shortUrl) {

    public Url(String id, String longUrl) {
        this(id, longUrl, null);
    }
}
