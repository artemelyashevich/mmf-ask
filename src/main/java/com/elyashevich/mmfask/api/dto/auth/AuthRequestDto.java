package com.elyashevich.mmfask.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AuthRequestDto(

        String email,

        @Length(
                min = 8,
                message = "Password must must contain {min} symbols."
        )
        String password
) {
}
