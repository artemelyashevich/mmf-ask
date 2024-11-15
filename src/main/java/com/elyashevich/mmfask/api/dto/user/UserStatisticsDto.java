package com.elyashevich.mmfask.api.dto.user;

import java.util.List;

public record UserStatisticsDto(
        Long countOfUsers,

        List<UserDto> users
) {
}
