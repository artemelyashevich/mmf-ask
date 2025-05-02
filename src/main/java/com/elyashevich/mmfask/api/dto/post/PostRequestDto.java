package com.elyashevich.mmfask.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Schema(
        name = "PostRequestDto",
        description = "Data Transfer Object for creating or updating a post"
)
public record PostRequestDto(

        @Schema(
                description = "Title of the post",
                example = "Introduction to Spring Boot",
                minLength = 2,
                maxLength = 255,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Title must be not empty.")
        @Length(
                min = 2,
                max = 255,
                message = "Title must be between {min} and {max} characters."
        )
        String title,

        @Schema(
                description = "Detailed description of the post",
                example = "This post covers the basics of Spring Boot framework",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Description must be not empty.")
        @Length(
                min = 2,
                message = "Description must be more than {min} characters."
        )
        String description,

        @Schema(
                description = "Email of the post creator",
                example = "user@example.com",
                format = "email",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Creator email must be not empty.")
        @Email(message = "Invalid email format.")
        String creatorEmail,

        @Schema(
                description = "Name of the associated programming language",
                example = "Java",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Programming language must be not empty.")
        @Length(
                min = 1,
                message = "Programming language must be more than {min} character."
        )
        String programmingLanguageName,

        @Schema(
                description = "Set of category names associated with the post",
                example = "[\"Backend\", \"Web Development\"]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Names of categories must be not empty.")
        Set<String> namesOfCategories,

        @Schema(
                description = "Number of views for the post",
                example = "150",
                defaultValue = "0"
        )
        Long views
) {
}