package com.elyashevich.mmfask.api.dto.post;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.api.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(
        name = "PostResponseDto",
        description = "Complete post data with all related entities"
)
public record PostResponseDto(

        @Schema(
                description = "Unique identifier of the post",
                example = "507f1f77bcf86cd799439011"
        )
        String id,

        @Schema(
                description = "Title of the post",
                example = "Understanding Spring Data JPA",
                minLength = 2,
                maxLength = 255
        )
        String title,

        @Schema(
                description = "Detailed content of the post",
                example = "This article explains the core concepts of Spring Data JPA..."
        )
        String description,

        @Schema(
                description = "Author information",
                implementation = UserDto.class
        )
        UserDto creator,

        @Schema(
                description = "Associated programming language details",
                implementation = ProgrammingLanguageDto.class
        )
        ProgrammingLanguageDto programmingLanguage,

        @Schema(
                description = "Set of categories this post belongs to",
                implementation = CategoryDto.class
        )
        Set<CategoryDto> categories,

        @Schema(
                description = "Number of times this post has been viewed",
                example = "1500",
                defaultValue = "0"
        )
        Long views,

        @Schema(
                description = "Set of image URLs associated with the post",
                example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]"
        )
        Set<String> images,

        @Schema(
                description = "Number of likes received",
                example = "42",
                defaultValue = "0"
        )
        Long likes,

        @Schema(
                description = "Number of dislikes received",
                example = "3",
                defaultValue = "0"
        )
        Long dislikes,

        @Schema(
                description = "Timestamp when the post was created",
                example = "2023-11-15T09:30:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Timestamp when the post was last updated",
                example = "2023-11-20T14:45:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime updatedAt
) {
}