package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.entity.Post;

/**
 * Mapper interface for mapping between Post and PostResponseDto objects.
 */
public interface PostMapper extends Mappable<Post, PostResponseDto> {

    /**
     * Converts a PostRequestDto object to a Post entity.
     *
     * @param dto the PostRequestDto to convert
     * @return the corresponding Post entity
     */
    Post toEntity(final PostRequestDto dto);
}