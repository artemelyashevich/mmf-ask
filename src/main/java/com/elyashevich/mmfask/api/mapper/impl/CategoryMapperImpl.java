package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto toDto(final Category entity) {
        return new CategoryDto(entity.getId(), entity.getName());
    }

    @Override
    public List<CategoryDto> toDto(final List<Category> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Set<CategoryDto> toDto(final Set<Category> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Category> toEntityFromStrings(Set<String> names) {
        return names.stream()
                .map(name -> Category.builder()
                        .name(name)
                        .build())
                .collect(Collectors.toSet());
    }

    @Override
    public Category toEntity(final CategoryDto dto) {
        return Category.builder()
                .name(dto.name())
                .build();
    }
}
