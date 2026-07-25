package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record GetUrlResponseDto(
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
