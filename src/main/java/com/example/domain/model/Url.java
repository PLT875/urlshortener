package com.example.domain.model;

import com.example.persistence.dao.UrlDao;

public record Url(String id, String longUrl, String shortUrl) {

    public static UrlDao toDao(Url url) {
        return new UrlDao(url.id(), url.longUrl());
    }
}
