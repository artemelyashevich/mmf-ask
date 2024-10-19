package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.CategoryController;
import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        var categories = this.categoryService.findAll();
        return this.categoryMapper.toDto(categories);
    }

    @Override
    public CategoryDto findById(final @PathVariable("id") String id) {
        var category = this.categoryService.findById(id);
        return this.categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto create(final @Validated @RequestBody CategoryDto categoryDto) {
        var category = this.categoryService.create(this.categoryMapper.toEntity(categoryDto));
        return this.categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(
            final @PathVariable("id") String id,
            final @Validated @RequestBody CategoryDto categoryDto
    ) {
        var category = this.categoryService.update(id, this.categoryMapper.toEntity(categoryDto));
        return this.categoryMapper.toDto(category);
    }

    @Override
    public void delete(final @PathVariable("id") String id) {
        this.categoryService.delete(id);
    }
}
