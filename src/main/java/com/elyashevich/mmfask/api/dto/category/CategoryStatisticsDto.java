package com.elyashevich.mmfask.api.dto.category;

import java.util.List;

public record CategoryStatisticsDto(
        Long countOfCategories,

        List<CategoryDto> categories
) {
}
