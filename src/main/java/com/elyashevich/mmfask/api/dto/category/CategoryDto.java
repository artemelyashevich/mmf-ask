package com.elyashevich.mmfask.api.dto.category;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CategoryDto(

        String id,

        @NotNull(message = "Name must be not empty.")
        @Length(
                min = 1,
                message = "Name must be more then {min}."
        )
        String name
) {
}
