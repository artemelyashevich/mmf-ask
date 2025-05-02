package com.elyashevich.mmfask.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "ActivationDto",
        description = "Data Transfer Object for account activation requests"
)
public record ActivationDto(

        @Schema(
                description = "Email address associated with the account to activate",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "email"
        )
        @NotNull(message = "Email must not be empty")
        @Email(message = "Must be a valid email address")
        String email
) {
}