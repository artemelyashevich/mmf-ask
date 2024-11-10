package com.elyashevich.mmfask.api.dto.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(

        String id,

        String userId,

        String postId,

        String body,

        Long like,

        Long dislike,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
