package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.CategoryRepository;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.converter.impl.CategoryConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter converter;

    @Override
    public Page<Category> findAll(String query, Integer page, Integer size, String sortDirection, String sortField) {
        log.debug("Attempting find All posts");
        var pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return this.categoryRepository.findByNameContainingIgnoreCase(query, pageable);
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
        log.debug("Attempting to create a category with name '{}'.", dto.getName());

        var name = dto.getName();
        if (this.categoryRepository.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Category with name = %s already exists.".formatted(name));
        }
        dto.setPosts(new ArrayList<>());
        var category = this.categoryRepository.save(dto);

        log.info("Category with name '{}' has been created.", dto.getName());
        return category;
    }

    @Transactional
    @Override
    public Category update(final String id, final Category dto) {
        log.debug("Attempting to update a category with name '{}'.", dto.getName());

        var candidate = this.findById(id);
        var category = this.converter.update(candidate, dto);
        var updatedCategory = this.categoryRepository.save(category);

        log.info("Category with name '{}' has been updated.", dto.getName());
        return updatedCategory;
    }

    @Transactional
    @Override
    public void delete(final String id) {
        log.debug("Attempting to delete a category with ID '{}'.", id);

        var candidate = this.findById(id);
        this.categoryRepository.delete(candidate);

        log.info("Category with ID '{}' has been deleted.", id);
    }
}
