package com.example.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

public record CreateUrlResponseDto(
        @NonNull
        @JsonProperty("key")
        String key,

        @NonNull
        @JsonProperty("long_url")
        String longUrl,

        @NonNull
        @JsonProperty("short_url")
        String shortUrl) {
}
