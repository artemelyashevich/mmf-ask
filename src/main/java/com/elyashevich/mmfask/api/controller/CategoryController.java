package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> findAll() {
        var categories = this.categoryService.findAll();
        return this.categoryMapper.toDto(categories);
    }

    @GetMapping("/{id}")
    public CategoryDto findById(final @PathVariable("id") String id) {
        var category = this.categoryService.findById(id);
        return this.categoryMapper.toDto(category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(final @Validated @RequestBody CategoryDto categoryDto) {
        var category = this.categoryService.create(this.categoryMapper.toEntity(categoryDto));
        return this.categoryMapper.toDto(category);
    }

    @PutMapping("/{id}")
    public CategoryDto update(
            final @PathVariable("id") String id,
            final @Validated @RequestBody CategoryDto categoryDto
    ) {
        var category = this.categoryService.update(id, this.categoryMapper.toEntity(categoryDto));
        return this.categoryMapper.toDto(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(final @PathVariable("id") String id) {
        this.categoryService.delete(id);
    }
 }
