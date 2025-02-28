package com.example.domain.model;

import com.example.persistence.entity.UrlEntity;

public record Url(String id, String longUrl, String shortUrl) {

    public static UrlEntity toEntity(Url url) {
        return new UrlEntity(url.id(), url.longUrl());
    }
}
