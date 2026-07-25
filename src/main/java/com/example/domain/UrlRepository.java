package com.example.domain;

import com.example.domain.model.Url;
import java.util.Optional;

public interface UrlRepository {
    Optional<Url> findById(String id);
    void save(Url url);
    void delete(String id);
}
