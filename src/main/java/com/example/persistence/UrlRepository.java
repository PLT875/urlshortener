package com.example.persistence;

import com.example.persistence.dao.UrlDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<UrlDao, String> {

}
