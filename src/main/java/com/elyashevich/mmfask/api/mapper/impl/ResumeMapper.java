package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.resume.ResumeCreateDto;
import com.elyashevich.mmfask.api.dto.resume.ResumeResponseDto;
import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.entity.Resume;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResumeMapper {

    public ResumeResponseDto toDto(Resume entity) {
        return new ResumeResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getProgrammingLanguages(),
                entity.getCategories(),
                entity.getExperienceInYears(),
                entity.getLevel()
        );
    }

    public List<ResumeResponseDto> toDto(List<Resume> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    public Resume toEntity(ResumeCreateDto dto) {
        return Resume.builder()
                .userId(dto.userId())
                .experienceInYears(dto.experienceInYears())
                .categories(dto.categoryIds().stream()
                        .map(it -> Category.builder().id(it).build())
                        .collect(Collectors.toSet())
                )
                .programmingLanguages(dto.programmingLanguageIds().stream()
                        .map(it -> ProgrammingLanguage.builder().id(it).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
