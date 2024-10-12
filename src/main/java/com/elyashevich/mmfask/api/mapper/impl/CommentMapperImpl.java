package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.api.dto.comment.CommentResponseDto;
import com.elyashevich.mmfask.api.mapper.CommentMapper;
import com.elyashevich.mmfask.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponseDto toResponseDto(final Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getPost().getId(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    @Override
    public List<CommentResponseDto> toResponseDto(final List<Comment> comments) {
        return comments.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
