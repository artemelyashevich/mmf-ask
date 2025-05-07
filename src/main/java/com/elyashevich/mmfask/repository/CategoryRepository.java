package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(final String name);

    Boolean existsByName(final String name);

    Page<Category> findByNameContainingIgnoreCase(String query, PageRequest pageable);
}
