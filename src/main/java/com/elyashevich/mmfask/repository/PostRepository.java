package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    @Query("{title:{$regex:?0}}")
    List<Post> findBy(final String query);

    Optional<Post> findByTitle(final String title);

    List<Post> findByCreator(User byEmail);

    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
