package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProgrammingLanguageRepository extends MongoRepository<ProgrammingLanguage, String> {

    Optional<ProgrammingLanguage> findByName(final String name);

    Boolean existsByName(final String name);
}
