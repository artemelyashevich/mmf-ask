package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.entity.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapperImpl implements PostMapper {
    @Override
    public PostResponseDto toDto(final Post entity) {
        return null;
    }

    @Override
    public List<PostResponseDto> toDto(final List<Post> entities) {
        return null;
    }

    @Override
    public Post toEntity(final PostResponseDto dto) {
        return null;
    }

    @Override
    public Post toEntity(final PostRequestDto dto) {
        return null;
    }
}
