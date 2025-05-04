package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResumeRepository extends MongoRepository<Resume, String> {
    Optional<Resume> findByUserId(String userId);
}
