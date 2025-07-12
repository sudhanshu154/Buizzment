package com.buizzment.repository;

import com.buizzment.model.Org;
import com.buizzment.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgRepository extends MongoRepository<Org, String> {

    Optional<Org> findById(String id);

    Optional<Org> findByName(String name);
    Boolean existsByName(String name);
}