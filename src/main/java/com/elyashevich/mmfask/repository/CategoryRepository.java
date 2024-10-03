package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(final String name);

    Boolean existsByName(final String name);
}
