package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.PostReaction;
import com.elyashevich.mmfask.repository.PostReactionRepository;
import com.elyashevich.mmfask.service.PostReactionService;
import com.elyashevich.mmfask.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {

    private final PostReactionRepository repository;
    private final PostService postService;

    @Transactional
    public PostReaction.ReactionType toggleReaction(String email, String postId, PostReaction.ReactionType newType) {
        var existing = repository.findByEmailAndPostId(email, postId);

        if (existing.isPresent()) {
            var current = existing.get();
            if (current.getType() == newType) {
                repository.delete(current);
                if (newType.equals(PostReaction.ReactionType.LIKE)) {
                    this.postService.undoLike(postId);
                } else {
                    this.postService.undoDislike(postId);
                }
                return null;
            } else {
                current.setType(newType);
                current.setCreatedAt(LocalDateTime.now());
                repository.save(current);
                if (newType.equals(PostReaction.ReactionType.LIKE)) {
                    this.postService.like(postId);
                } else {
                    this.postService.dislike(postId);
                }
                return newType;
            }
        } else {
            var reaction = new PostReaction();
            reaction.setEmail(email);
            reaction.setPostId(postId);
            reaction.setType(newType);
            reaction.setCreatedAt(LocalDateTime.now());
            repository.save(reaction);
            if (newType.equals(PostReaction.ReactionType.LIKE)) {
                this.postService.like(postId);
            } else {
                this.postService.dislike(postId);
            }
            return newType;
        }
    }

    public Map<String, Long> getReactionCounts(String postId) {
        var likes = repository.countByPostIdAndType(postId, PostReaction.ReactionType.LIKE);
        var dislikes = repository.countByPostIdAndType(postId, PostReaction.ReactionType.DISLIKE);
        return Map.of("likes", likes, "dislikes", dislikes);
    }

    public PostReaction.ReactionType getUserReaction(String userId, String postId) {
        return repository.findByEmailAndPostId(userId, postId)
                .map(PostReaction::getType)
                .orElse(null);
    }
}
