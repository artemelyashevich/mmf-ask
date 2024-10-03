package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(final String email);

    Boolean existsByEmail(final String email);
}
