package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    @Query("{title:{$regex:?0}}")
    Page<Post> findBy(final String query, final Pageable page);


    Optional<Post> findByTitle(final String title);
}
