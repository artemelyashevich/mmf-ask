package com.elyashevich.mmfask.api.dto.post;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto(

        String id,

        String title,

        String description,

        ProgrammingLanguageDto programmingLanguage,

        Set<CategoryDto> categories,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
