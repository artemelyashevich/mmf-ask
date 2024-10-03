package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findByTitle(final String title);
}
