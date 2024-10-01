package com.elyashevich.mmfask.api.dto;

public record UserDto(
        String id,
        String email,
        String createdAt,
        String updatedAt
) {
}
