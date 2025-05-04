package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.service.contract.CrudService;

import java.util.List;

/**
 * Service interface for managing categories.
 */
public interface CategoryService extends CrudService<Category> {

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    List<Category> findAll();

    /**
     * Finds a category by name.
     *
     * @param name the name of the category to find
     * @return the found category
     */
    Category findByName(final String name);
}
