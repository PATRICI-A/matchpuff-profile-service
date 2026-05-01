package com.matchpuff.profileservice.infrastructure.adapters.persistence.repository;

import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(String email);
}
