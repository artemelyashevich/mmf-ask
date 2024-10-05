package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;

/**
 * Mapper interface for mapping between ProgrammingLanguage and ProgrammingLanguageDto objects.
 */
public interface ProgrammingLanguageMapper extends Mappable<ProgrammingLanguage, ProgrammingLanguageDto> {

    /**
     * Converts a programming language name to a ProgrammingLanguage entity.
     *
     * @param name the name of the programming language to convert
     * @return the corresponding ProgrammingLanguage entity
     */
    ProgrammingLanguage toEntityFromString(final String name);
}
