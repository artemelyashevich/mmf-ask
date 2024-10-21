package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.ActivationCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ActivationCodeRepository extends MongoRepository<ActivationCode, String> {

    Optional<ActivationCode> findByEmail(final String email);
}
