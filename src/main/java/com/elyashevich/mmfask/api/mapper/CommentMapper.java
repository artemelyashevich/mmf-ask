package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.comment.CommentResponseDto;
import com.elyashevich.mmfask.entity.Comment;

import java.util.List;

/**
 * Mapper interface for mapping between Comment and CommentResponseDto objects.
 */
public interface CommentMapper {

    CommentResponseDto toResponseDto(final Comment comment);

    List<CommentResponseDto> toResponseDto(final List<Comment> comments);
}
