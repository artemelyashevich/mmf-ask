package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryDto toDto(final Category entity) {
        return null;
    }

    @Override
    public List<CategoryDto> toDto(final List<Category> entities) {
        return null;
    }

    @Override
    public Category toEntity(final CategoryDto dto) {
        return null;
    }
}
