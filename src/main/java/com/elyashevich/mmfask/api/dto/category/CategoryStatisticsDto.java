package com.elyashevich.mmfask.api.dto.category;

import java.util.List;
import java.util.Map;

public record CategoryStatisticsDto(
        Long countOfCategories,

        List<CategoryDto> categories,

        Map<String, Long> mostPopular
) {
}
