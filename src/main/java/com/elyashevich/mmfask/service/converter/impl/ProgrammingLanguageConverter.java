package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.service.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProgrammingLanguageConverter implements Converter<ProgrammingLanguage> {

    @Override
    public ProgrammingLanguage update(ProgrammingLanguage oldEntity, ProgrammingLanguage newEntity) {
        oldEntity.setName(newEntity.getName());
        return oldEntity;
    }
}
