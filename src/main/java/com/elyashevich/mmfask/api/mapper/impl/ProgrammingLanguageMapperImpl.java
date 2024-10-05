package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.api.mapper.ProgrammingLanguageMapper;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProgrammingLanguageMapperImpl implements ProgrammingLanguageMapper {

    @Override
    public ProgrammingLanguageDto toDto(final ProgrammingLanguage entity) {
        return new ProgrammingLanguageDto(entity.getId(), entity.getName());
    }

    @Override
    public List<ProgrammingLanguageDto> toDto(final List<ProgrammingLanguage> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ProgrammingLanguage toEntity(final ProgrammingLanguageDto dto) {
        return ProgrammingLanguage.builder()
                .name(dto.name())
                .build();
    }

    @Override
    public ProgrammingLanguage toEntityFromString(String name) {
        return ProgrammingLanguage.builder()
                .name(name)
                .build();
    }
}
