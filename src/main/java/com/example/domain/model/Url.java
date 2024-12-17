package com.example.domain.model;

import com.example.persistence.dao.UrlDao;

public record Url(String id, String longUrl, String shortUrl) {

    public static UrlDao toUrlDao(Url url) {
        return new UrlDao(url.id(), url.longUrl(), url.shortUrl());
    }
}
