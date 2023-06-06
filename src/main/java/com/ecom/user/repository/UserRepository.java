package com.ecom.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecom.user.entities.User;

public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> findByEmail(String email);
    
}
