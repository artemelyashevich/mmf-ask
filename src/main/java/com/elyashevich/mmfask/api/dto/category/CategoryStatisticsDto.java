package com.elyashevich.mmfask.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(
        name = "CategoryStatisticsDto",
        description = "DTO containing statistical information about categories"
)
public record CategoryStatisticsDto(

        @Schema(
                description = "Total count of categories available",
                example = "15",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long countOfCategories,

        @Schema(
                description = "List of category details",
                implementation = CategoryDto.class
        )
        List<CategoryDto> categories,

        @Schema(
                description = "Map of most popular categories with their usage count",
                example = "{\"Web Development\": 42, \"Data Science\": 25, \"Mobile\": 18}",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Map<String, Long> mostPopular
) {
}