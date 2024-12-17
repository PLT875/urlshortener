package com.example.presentation.controller.dto;

import com.example.domain.model.Url;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUrlResponseDto(
        @JsonProperty("key") String key,
        @JsonProperty("long_url") String longUrl,
        @JsonProperty("short_url") String shortUrl
) {
    public static CreateUrlResponseDto from(Url url) {
        return new CreateUrlResponseDto(url.id(), url.longUrl(), url.shortUrl());
    }
}
