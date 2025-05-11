package com.elyashevich.mmfask.api.dto.user;

import com.elyashevich.mmfask.entity.Badge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record UserDto(

        String id,

        String email,

        String image,

        Map<String, Integer> stats,

        List<Badge> badges,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}