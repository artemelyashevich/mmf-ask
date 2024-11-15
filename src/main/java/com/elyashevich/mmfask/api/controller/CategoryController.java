package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.category.CategoryDto;
import com.elyashevich.mmfask.api.dto.category.CategoryStatisticsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

/**
 * Controller for managing categories.
 */
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Controller", description = "APIs for managing categories")
public interface CategoryController {

    /**
     * Find all categories.
     *
     * @return List of CategoryDto objects representing categories.
     */
    @Operation(
            summary = "Find all categories",
            description = "Get a list of all categories"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories found",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @GetMapping
    List<CategoryDto> findAll();

    /**
     * Find a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return CategoryDto object representing the category.
     */
    @Operation(
            summary = "Find category by ID",
            description = "Get details of a category by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category found",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @GetMapping("/{id}")
    CategoryDto findById(final @PathVariable("id") String id);

    /**
     * Create a new category.
     *
     * @param categoryDto The CategoryDto containing the new category data.
     * @return CategoryDto object representing the created category.
     */
    @Operation(
            summary = "Create a new category",
            description = "Create a new category"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Category created",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto create(final @Validated @RequestBody CategoryDto categoryDto);

    /**
     * Update an existing category by its ID.
     *
     * @param id The ID of the category to update.
     * @param categoryDto The updated CategoryDto data.
     * @return CategoryDto object representing the updated category.
     */
    @Operation(
            summary = "Update a category",
            description = "Update an existing category by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category updated",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @PutMapping("/{id}")
    CategoryDto update(
            final @PathVariable("id") String id,
            final @Validated @RequestBody CategoryDto categoryDto
    );

    @GetMapping("/statistics")
    CategoryStatisticsDto findStatistics();

    /**
     * Delete a category by its ID.
     *
     * @param id The ID of the category to delete.
     */
    @Operation(
            summary = "Delete a category",
            description = "Delete a category by ID")
    @ApiResponse(
            responseCode = "204",
            description = "Category deleted"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(final @PathVariable("id") String id);
}