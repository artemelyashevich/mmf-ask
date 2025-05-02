package com.elyashevich.mmfask.api.dto.programmingLanguage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(
        name = "ProgrammingLanguageDto",
        description = "Data Transfer Object for programming language information"
)
public record ProgrammingLanguageDto(

        @Schema(
                description = "The unique identifier of the programming language",
                example = "65a1e5b8d4a3f12b3c4d5e6f"
        )
        String id,

        @Schema(
                description = "The name of the programming language",
                example = "Java",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Name must be not empty.")
        @Length(
                min = 1,
                message = "Name must be more then {min}."
        )
        String name,

        @Schema(
                description = "Description of the programming language",
                example = "A high-level, class-based, object-oriented programming language"
        )
        String description,

        @Schema(
                description = "Number of posts related to this programming language",
                example = "42"
        )
        Integer numberOfPosts
) {
}