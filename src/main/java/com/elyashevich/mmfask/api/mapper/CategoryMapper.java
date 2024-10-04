package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.entity.Category;

import java.util.Set;

public interface CategoryMapper extends Mappable<Category, CategoryDto> {

    Set<CategoryDto> toDto(final Set<Category> categories);

    Set<Category> toEntityFromStrings(final Set<String> names);
}
