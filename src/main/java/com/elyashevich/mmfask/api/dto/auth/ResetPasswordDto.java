package com.elyashevich.mmfask.api.dto.auth;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ResetPasswordDto(

        @NotNull(message = "Old password must be not empty.")
        @Length(
                min = 8,
                message = "Old Password must contain more then {min} symbols."
        )
        String oldPassword,

        @NotNull(message = "New password must be not empty.")
        @Length(
                min = 8,
                message = "New password must contain more then {min} symbols."
        )
        String newPassword
) {
}
