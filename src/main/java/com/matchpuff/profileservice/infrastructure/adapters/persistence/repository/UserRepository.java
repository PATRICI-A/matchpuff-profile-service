package com.matchpuff.profileservice.infrastructure.adapters.persistence.repository;

import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, UUID> {
    Optional<UserDocument> findByEmail(String email);
    List<UserDocument> findByUserType(UserType userType);
}
