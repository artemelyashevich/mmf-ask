package com.elyashevich.mmfask.api.dto.user;

import java.time.LocalDateTime;

public record UserDto(

        String id,

        String email,

        String image,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}