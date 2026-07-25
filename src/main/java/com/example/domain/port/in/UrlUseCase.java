package com.example.domain.port.in;

import com.example.domain.model.Url;

public interface UrlUseCase {
    Url getUrl(String id);
    Url createShortUrl(String longUrl);
}
