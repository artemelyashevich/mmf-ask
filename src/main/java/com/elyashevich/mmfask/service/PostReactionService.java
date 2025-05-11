package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.PostReaction;

import java.util.Map;

public interface PostReactionService {

    PostReaction.ReactionType toggleReaction(String email, String postId, PostReaction.ReactionType newType);

    Map<String, Long> getReactionCounts(String postId);

    PostReaction.ReactionType getUserReaction(String userId, String postId);
}
