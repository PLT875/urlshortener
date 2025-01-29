package com.example.presentation.controller.dto;

import com.example.domain.model.Url;
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
        String shortUrl
) {
    public static CreateUrlResponseDto from(Url url) {
        return new CreateUrlResponseDto(url.id(), url.longUrl(), url.shortUrl());
    }
}
