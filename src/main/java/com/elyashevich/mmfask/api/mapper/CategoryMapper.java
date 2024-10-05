package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.entity.Category;

import java.util.Set;

/**
 * Mapper interface for mapping between Category and CategoryDto objects.
 */
public interface CategoryMapper extends Mappable<Category, CategoryDto> {

    /**
     * Converts a set of Category objects to a set of CategoryDto objects.
     *
     * @param categories the set of Category objects to convert
     * @return a set of corresponding CategoryDto objects
     */
    Set<CategoryDto> toDto(final Set<Category> categories);

    /**
     * Converts a set of category names to a set of Category objects.
     *
     * @param names the set of category names to convert
     * @return a set of corresponding Category objects
     */
    Set<Category> toEntityFromStrings(final Set<String> names);
}
