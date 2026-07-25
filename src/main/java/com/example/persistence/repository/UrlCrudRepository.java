package com.example.persistence.repository;

import com.example.persistence.entity.UrlEntity;
import org.springframework.data.repository.CrudRepository;

public interface UrlCrudRepository extends CrudRepository<UrlEntity, String> {
}
