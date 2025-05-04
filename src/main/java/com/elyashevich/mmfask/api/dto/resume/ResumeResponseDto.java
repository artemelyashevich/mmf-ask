package com.elyashevich.mmfask.api.dto.resume;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(
        name = "ResumeResponseDto",
        description = "Data Transfer Object containing complete resume details"
)
public record ResumeResponseDto(

        @Schema(
                description = "Unique identifier of the resume",
                example = "507f1f77bcf86cd799439011"
        )
        String id,

        @Schema(
                description = "ID of the user who owns the resume",
                example = "6587a8e6a3f12b3c4d5e6f7a"
        )
        String userId,

        @Schema(
                description = "Set of programming languages associated with the resume",
                implementation = ProgrammingLanguage.class
        )
        Set<ProgrammingLanguage> programmingLanguages,

        @Schema(
                description = "Set of categories associated with the resume",
                implementation = Category.class
        )
        Set<Category> categories,

        @Schema(
                description = "Years of professional experience",
                example = "5",
                minimum = "1"
        )
        Integer experienceInYears,

        @Schema(
                description = "Seniority level calculated based on experience and skills",
                example = "3",
                minimum = "1",
                maximum = "10"
        )
        Integer level
) {
}