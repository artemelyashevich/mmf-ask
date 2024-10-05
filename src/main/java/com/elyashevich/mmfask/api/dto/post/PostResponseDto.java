package com.elyashevich.mmfask.api.dto.post;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto(

        String id,

        String title,

        String description,

        ProgrammingLanguageDto programmingLanguage,

        Set<CategoryDto> categories,

        Long views,

        Set<String> images,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
