package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
