package com.elyashevich.mmfask.api.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
        name = "CommentResponseDto",
        description = "Data Transfer Object containing comment details with engagement metrics"
)
public record CommentResponseDto(

        @Schema(
                description = "Unique identifier of the comment",
                example = "507f1f77bcf86cd799439011",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String id,

        @Schema(
                description = "ID of the user who created the comment",
                example = "6587a8e6a3f12b3c4d5e6f7a",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String userId,

        @Schema(
                description = "ID of the post this comment belongs to",
                example = "507f1f77bcf86cd799439012",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String postId,

        @Schema(
                description = "Text content of the comment",
                example = "This was really helpful, thanks for sharing!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String body,

        @Schema(
                description = "Number of likes received by the comment",
                example = "15",
                defaultValue = "0"
        )
        Long likes,

        @Schema(
                description = "Number of dislikes received by the comment",
                example = "2",
                defaultValue = "0"
        )
        Long dislikes,

        @Schema(
                description = "Timestamp when the comment was created",
                example = "2023-11-15T09:30:00",
                type = "string",
                format = "date-time",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Timestamp when the comment was last updated",
                example = "2023-11-16T14:45:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime updatedAt
) {
}