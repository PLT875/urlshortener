package com.example.infrastructure.adapter.out.persistence;

import com.example.domain.port.out.UrlRepository;
import com.example.domain.model.Url;
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
        JpaUrlEntity entity = toEntity(url);
        urlCrudRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        urlCrudRepository.deleteById(id);
    }

    private JpaUrlEntity toEntity(Url url) {
        return new JpaUrlEntity(url.id(), url.longUrl());
    }

    private Url toDomain(JpaUrlEntity entity) {
        return new Url(entity.getId(), entity.getLongUrl());
    }
}
