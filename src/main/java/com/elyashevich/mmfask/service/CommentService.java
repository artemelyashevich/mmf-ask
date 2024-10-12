package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll(final String userId, final String postId);

    Comment findById(final String id);

    Comment create(final CommentRequestDto dto, final String email);

    Comment update (final String id, final CommentRequestDto dto);

    void delete(final String id);
}
