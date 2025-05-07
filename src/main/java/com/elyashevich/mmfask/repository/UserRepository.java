package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(final String email);

    Boolean existsByEmail(final String email);

    Page<User> findByEmailContainingIgnoreCase(String query, Pageable pageable);
}
