package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.service.contract.CrudService;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing categories.
 */
public interface CategoryService extends CrudService<Category> {

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    Page<Category> findAll(String query, Integer page, Integer size, String sortDirection, String sortField);

    /**
     * Finds a category by name.
     *
     * @param name the name of the category to find
     * @return the found category
     */
    Category findByName(final String name);
}
