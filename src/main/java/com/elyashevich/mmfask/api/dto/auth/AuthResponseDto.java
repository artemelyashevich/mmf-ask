package com.elyashevich.mmfask.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AuthResponseDto",
        description = "Authentication response containing JWT token"
)
public record AuthResponseDto(

        @Schema(
                description = "JSON Web Token (JWT) for authenticated requests",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String token
) {
}