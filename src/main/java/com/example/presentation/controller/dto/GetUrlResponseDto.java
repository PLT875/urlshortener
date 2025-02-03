package com.example.presentation.controller.dto;

import com.example.domain.model.Url;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record GetUrlResponseDto(
        @NonNull
        @JsonProperty("key")
        String key,

        @NonNull
        @JsonProperty("short_url")
        String shortUrl,

        @NonNull
        @JsonProperty("long_url")
        String longUrl) {

    public static GetUrlResponseDto from(Url url) {
        return GetUrlResponseDto
                .builder()
                .key(url.id())
                .shortUrl(url.shortUrl())
                .longUrl(url.longUrl())
                .build();
    }

}
