package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.service.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<Category> {

    @Override
    public Category update(Category oldEntity, Category newEntity) {
        oldEntity.setName(newEntity.getName());
        return oldEntity;
    }
}
