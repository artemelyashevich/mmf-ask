package com.elyashevich.mmfask.api.dto.resume;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;

import java.util.Set;

public record ResumeResponseDto(
        String id,
        String userId,
        Set<ProgrammingLanguage> programmingLanguages,
        Set<Category> categories,
        Integer experienceInYears,
        Integer level
) {
}
