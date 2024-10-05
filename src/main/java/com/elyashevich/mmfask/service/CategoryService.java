package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Category;

import java.util.List;

/**
 * Service interface for managing categories.
 */
public interface CategoryService {

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

    /**
     * Finds a category by ID.
     *
     * @param id the ID of the category to find
     * @return the found category
     */
    Category findById(final String id);

    /**
     * Creates a new category.
     *
     * @param dto the category DTO to create
     * @return the created category
     */
    Category create(final Category dto);

    /**
     * Updates a category by ID.
     *
     * @param id the ID of the category to update
     * @param dto the updated category DTO
     * @return the updated category
     */
    Category update(final String id, Category dto);

    /**
     * Deletes a category by ID.
     *
     * @param id the ID of the category to delete
     */
    void delete(final String id);
}
