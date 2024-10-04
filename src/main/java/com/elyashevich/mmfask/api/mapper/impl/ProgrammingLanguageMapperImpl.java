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
        return null;
    }

    @Override
    public List<ProgrammingLanguageDto> toDto(final List<ProgrammingLanguage> entities) {
        return null;
    }

    @Override
    public ProgrammingLanguage toEntity(final ProgrammingLanguageDto dto) {
        return null;
    }
}
