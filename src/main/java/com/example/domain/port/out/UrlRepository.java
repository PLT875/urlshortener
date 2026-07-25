package com.example.domain.port.out;

import com.example.domain.model.Url;
import java.util.Optional;

public interface UrlRepository {
    Optional<Url> findById(String id);
    void save(Url url);
    void delete(String id);
}
