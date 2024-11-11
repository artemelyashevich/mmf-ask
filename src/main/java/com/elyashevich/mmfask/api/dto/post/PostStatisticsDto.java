package com.elyashevich.mmfask.api.dto.post;

import java.util.List;

public record PostStatisticsDto(
        long countOfPosts,

        double averageRating,

        List<PostResponseDto> posts
) {
}
