package com.elyashevich.mmfask.api.dto.favorites;

import com.elyashevich.mmfask.api.dto.post.PostResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record FavoritesDto(

        String id,

        String userId,

        List<PostResponseDto> posts,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
