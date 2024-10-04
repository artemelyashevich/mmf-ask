package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.entity.Post;

public interface PostMapper extends Mappable<Post, PostResponseDto> {

    Post toEntity(final PostRequestDto dto);
}
