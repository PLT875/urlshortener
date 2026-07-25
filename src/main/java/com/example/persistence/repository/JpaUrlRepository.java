package com.example.persistence.repository;

import com.example.domain.UrlRepository;
import com.example.domain.model.Url;
import com.example.persistence.entity.UrlEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUrlRepository implements UrlRepository {

    private final UrlCrudRepository urlCrudRepository;

    @Override
    public Optional<Url> findById(String id) {
        return urlCrudRepository.findById(id)
            .map(this::toDomain);
    }

    @Override
    public void save(Url url) {
        UrlEntity entity = toEntity(url);
        urlCrudRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        urlCrudRepository.deleteById(id);
    }

    private UrlEntity toEntity(Url url) {
        return new UrlEntity(url.id(), url.longUrl());
    }

    private Url toDomain(UrlEntity entity) {
        return new Url(entity.getId(), entity.getLongUrl());
    }
}
