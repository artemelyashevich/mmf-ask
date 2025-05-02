package com.elyashevich.mmfask.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(
        name = "ResetPasswordDto",
        description = "Data Transfer Object for password reset operation"
)
public record ResetPasswordDto(

        @Schema(
                description = "Current password of the user",
                example = "OldSecurePassword123!",
                minLength = 8,
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "password"
        )
        @NotNull(message = "Current password must not be empty.")
        @Length(
                min = 8,
                message = "Current password must be at least {min} characters long."
        )
        String oldPassword,

        @Schema(
                description = "New password to set",
                example = "NewSecurePassword456!",
                minLength = 8,
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "password"
        )
        @NotNull(message = "New password must not be empty.")
        @Length(
                min = 8,
                message = "New password must be at least {min} characters long."
        )
        String newPassword
) {
}