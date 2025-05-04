package com.elyashevich.mmfask.api.dto.resume;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;

@Schema(
        name = "ResumeCreateDto",
        description = "Data Transfer Object for creating or updating a resume"
)
public record ResumeCreateDto(

        @Schema(
                description = "ID of the user who owns the resume",
                example = "6587a8e6a3f12b3c4d5e6f7a",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "User ID must not be null")
        String userId,

        @Schema(
                description = "Set of category IDs associated with the resume",
                example = "[\"65a1e5b8d4a3f12b3c4d5e6f\", \"65a1e5b8d4a3f12b3c4d5e7g\"]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Category IDs must not be null")
        Set<String> categoryIds,

        @Schema(
                description = "Set of programming language IDs associated with the resume",
                example = "[\"65a1e5b8d4a3f12b3c4d5e8h\", \"65a1e5b8d4a3f12b3c4d5e9i\"]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Programming language IDs must not be null")
        Set<String> programmingLanguageIds,

        @Schema(
                description = "Years of professional experience",
                example = "5",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Experience in years must not be null")
        @Positive(message = "Experience must be a positive number")
        Integer experienceInYears
) {
}