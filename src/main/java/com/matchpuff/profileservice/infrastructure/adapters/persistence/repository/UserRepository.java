package com.matchpuff.profileservice.infrastructure.adapters.persistence.repository;

import com.matchpuff.profileservice.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
}
