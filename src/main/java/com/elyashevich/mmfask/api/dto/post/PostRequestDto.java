package com.elyashevich.mmfask.api.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record PostRequestDto(

        @NotNull(message = "Title must be not empty.")
        @Length(
                min = 2,
                max = 255,
                message = "Title must be in {min} and {max}."
        )
        String title,

        @NotNull(message = "Description must be not empty.")
        @Length(
                min = 2,
                message = "Description must be more then {min}."
        )
        String description,

        @NotNull(message = "Programming language must be not empty.")
        @Length(
                min = 1,
                message = "Programming language must be more then {min}."
        )
        String programmingLanguageName,

        @NotNull(message = "Names of categories must be not empty.")
        Set<String> namesOfCategories
) {
}
