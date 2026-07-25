package com.example.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.CrudRepository;

public interface UrlCrudRepository extends CrudRepository<JpaUrlEntity, String> {
}
