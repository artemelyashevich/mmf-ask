package com.elyashevich.mmfask.api.dto.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(

        String id,

        String userId,

        String postId,

        String body,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
