package com.elyashevich.mmfask.api.dto.post;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.api.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto(

        String id,

        String title,

        String description,

        UserDto creator,

        ProgrammingLanguageDto programmingLanguage,

        Set<CategoryDto> categories,

        Long views,

        Set<String> images,

        Long likes,

        Long dislikes,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
