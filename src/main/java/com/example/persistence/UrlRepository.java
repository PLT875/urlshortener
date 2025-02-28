package com.example.persistence;

import com.example.persistence.entity.UrlEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<UrlEntity, String> {

}
