package com.elyashevich.mmfask.api.dto.resume;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record ResumeCreateDto(

        @NotNull(message = "User Id must be not null")
        String userId,

        @NotNull(message = "Categories ids must be not null")
        Set<String> categoryIds,

        @NotNull(message = "Programming languages ids must be not null")
        Set<String> programmingLanguageIds,

        @NotNull(message = "Experience in years must be not null")
        @Positive(message = "Experience in years must be positive")
        Integer experienceInYears
) {
}
