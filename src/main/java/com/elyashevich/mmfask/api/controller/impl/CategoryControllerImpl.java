package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.CategoryController;
import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.dto.category.CategoryStatisticsDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {

    private final StatisticService statisticService;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(String query, Integer page, Integer size, String sortDirection, String sortField) {
        var categories = this.categoryService.findAll(query, page, size, sortDirection, sortField);
        return categories.map(this.categoryMapper::toDto);
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
    public CategoryStatisticsDto findStatistics() {
        return this.statisticService.categoryStatistics();
    }

    @Override
    public void delete(final @PathVariable("id") String id) {
        this.categoryService.delete(id);
    }
}
