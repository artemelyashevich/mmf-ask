package com.elyashevich.mmfask.api.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(
        name = "CommentRequestDto",
        description = "Data Transfer Object for creating or updating a comment"
)
public record CommentRequestDto(

        @Schema(
                description = "ID of the post being commented on",
                example = "507f1f77bcf86cd799439012",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Post id must not be empty.")
        String postId,

        @Schema(
                description = "Text content of the comment",
                example = "This post was really helpful!",
                minLength = 2,
                maxLength = 500,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Body must not be empty.")
        @Length(
                min = 2,
                max = 500,
                message = "Comment body must be between {min} and {max} characters."
        )
        String body
) {
}