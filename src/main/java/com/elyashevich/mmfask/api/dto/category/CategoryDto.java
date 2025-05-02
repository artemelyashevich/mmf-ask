package com.elyashevich.mmfask.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(
        name = "CategoryDto",
        description = "Data Transfer Object representing a post category"
)
public record CategoryDto(

        @Schema(
                description = "Unique identifier of the category",
                example = "507f1f77bcf86cd799439011"
        )
        String id,

        @Schema(
                description = "Name of the category",
                example = "Web Development",
                minLength = 1,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Name must not be empty.")
        @Length(
                min = 1,
                message = "Name must be at least {min} character long."
        )
        String name
) {
}