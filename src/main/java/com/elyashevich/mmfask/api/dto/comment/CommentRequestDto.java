package com.elyashevich.mmfask.api.dto.comment;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CommentRequestDto(

        @NotNull(message = "Post id must be not empty.")
        String postId,

        @NotNull(message = "Body must be not empty.")
        @Length(
                min = 2,
                max = 500,
                message = "Body must be in {min} and {max}."
        )
        String body
) {
}
