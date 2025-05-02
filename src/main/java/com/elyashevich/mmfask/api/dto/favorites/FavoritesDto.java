package com.elyashevich.mmfask.api.dto.favorites;

import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
        name = "FavoritesDto",
        description = "Data Transfer Object representing a user's favorite posts collection"
)
public record FavoritesDto(

        @Schema(
                description = "Unique identifier of the favorites collection",
                example = "507f1f77bcf86cd799439011"
        )
        String id,

        @Schema(
                description = "ID of the user who owns this favorites collection",
                example = "6587a8e6a3f12b3c4d5e6f7a"
        )
        String userId,

        @Schema(
                description = "List of favorite posts",
                implementation = PostResponseDto.class
        )
        List<PostResponseDto> posts,

        @Schema(
                description = "Timestamp when the favorites collection was created",
                example = "2023-12-25T10:15:30",
                type = "string",
                format = "date-time"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Timestamp when the favorites collection was last updated",
                example = "2024-01-15T14:30:45",
                type = "string",
                format = "date-time"
        )
        LocalDateTime updatedAt
) {
}