package com.elyashevich.mmfask.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(
        name = "AuthRequestDto",
        description = "Data Transfer Object for authentication requests"
)
public record AuthRequestDto(

        @Schema(
                description = "User's email address",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "email"
        )
        @NotNull(message = "Email must not be null")
        @Email(message = "Must be a valid email address")
        String email,

        @Schema(
                description = "User's password",
                example = "SecurePassword123!",
                minLength = 8,
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "password"
        )
        @NotNull(message = "Password must not be null")
        @Length(
                min = 8,
                message = "Password must be at least {min} characters long"
        )
        String password
) {
}