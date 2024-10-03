package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.CategoryRepository;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.converter.impl.CategoryConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter converter;

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findByName(final String name) {
        return this.categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with name = %s was not found.".formatted(name))
                );
    }

    @Override
    public Category findById(final String id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id = %s was not found.".formatted(id))
                );
    }

    @Transactional
    @Override
    public Category create(final Category dto) {
        var name = dto.getName();
        if (this.categoryRepository.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Category with name = %s already exists.".formatted(name));
        }
        return this.categoryRepository.save(dto);
    }

    @Transactional
    @Override
    public Category update(final String id, final Category dto) {
        var candidate = this.findById(id);
        var category = this.converter.update(candidate, dto);
        return this.categoryRepository.save(category);
    }

    @Transactional
    @Override
    public void delete(final String id) {
        var candidate = this.findById(id);
        this.categoryRepository.delete(candidate);
    }
}
