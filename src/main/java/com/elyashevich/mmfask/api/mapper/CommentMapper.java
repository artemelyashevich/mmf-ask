package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.comment.CommentResponseDto;
import com.elyashevich.mmfask.entity.Comment;

import java.util.List;

/**
 * Mapper interface for mapping between Comment and CommentResponseDto objects.
 */
public interface CommentMapper {

    /**
     * Converts a Comment object to a CommentResponseDto object.
     *
     * @param comment the Comment object to be converted
     * @return the corresponding CommentResponseDto object
     */
    CommentResponseDto toResponseDto(final Comment comment);

    /**
     * Converts a list of Comment objects to a list of CommentResponseDto objects.
     *
     * @param comments the list of Comment objects to be converted
     * @return the corresponding list of CommentResponseDto objects
     */
    List<CommentResponseDto> toResponseDto(final List<Comment> comments);
}
