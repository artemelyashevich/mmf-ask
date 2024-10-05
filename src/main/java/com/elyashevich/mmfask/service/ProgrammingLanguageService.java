package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.ProgrammingLanguage;

import java.util.List;

/**
 * Service interface for managing programming languages.
 */
public interface ProgrammingLanguageService {

    /**
     * Retrieves all programming languages.
     *
     * @return a list of all programming languages
     */
    List<ProgrammingLanguage> findAll();

    /**
     * Finds a programming language by name.
     *
     * @param name the name of the programming language to find
     * @return the found programming language
     */
    ProgrammingLanguage findByName(final String name);

    /**
     * Finds a programming language by ID.
     *
     * @param id the ID of the programming language to find
     * @return the found programming language
     */
    ProgrammingLanguage findById(final String id);

    /**
     * Creates a new programming language.
     *
     * @param dto the programming language DTO to create
     * @return the created programming language
     */
    ProgrammingLanguage create(final ProgrammingLanguage dto);

    /**
     * Updates a programming language by ID.
     *
     * @param id the ID of the programming language to update
     * @param dto the updated programming language DTO
     * @return the updated programming language
     */
    ProgrammingLanguage update(final String id, ProgrammingLanguage dto);

    /**
     * Deletes a programming language by ID.
     *
     * @param id the ID of the programming language to delete
     */
    void delete(final String id);
}
