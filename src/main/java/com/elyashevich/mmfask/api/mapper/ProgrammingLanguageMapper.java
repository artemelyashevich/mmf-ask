package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;

public interface ProgrammingLanguageMapper extends Mappable<ProgrammingLanguage, ProgrammingLanguageDto> {

    ProgrammingLanguage toEntityFromString(final String name);
}
