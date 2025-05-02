package com.elyashevich.mmfask.api.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Schema(
        name = "ExceptionBody",
        description = "Standardized error response body containing error details"
)
@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {

    @Schema(
            description = "Main error message describing the failure",
            example = "Validation failed",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final String message;

    @Schema(
            description = "Map of field-specific validation errors (field name -> error message)",
            example = "{\"email\": \"Invalid email format\", \"password\": \"Password must be at least 8 characters\"}",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final Map<String, String> errors;

    public ExceptionBody(String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }
}