package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.PostReaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostReactionRepository extends MongoRepository<PostReaction, String> {
    Optional<PostReaction> findByEmailAndPostId(String userId, String postId);

    void deleteByEmailAndPostId(String userId, String postId);

    long countByPostIdAndType(String postId, PostReaction.ReactionType type);
}
