package com.elyashevich.mmfask.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ActivationDto(

        @NotNull(message = "Email must be not empty.")
        @Email(message = "Invalid email format.")
        String email
) {
}
