package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
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
 * API controller for managing programming languages.
 */
@RequestMapping("/api/v1/programming-languages")
@Tag(name = "Programming Language Controller", description = "APIs for managing programming languages")
public interface ProgrammingLanguageController {

    /**
     * Find all programming languages.
     *
     * @return List of ProgrammingLanguageDto objects representing all programming languages.
     */
    @Operation(
            summary = "Find all programming languages",
            description = "Get a list of all programming languages"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Programming languages found",
            content = @Content(schema = @Schema(implementation = List.class))
    )
    @GetMapping
    List<ProgrammingLanguageDto> findAll();

    /**
     * Find programming language by ID.
     *
     * @param id The ID of the programming language to find.
     * @return ProgrammingLanguageDto object representing the programming language with the specified ID.
     */
    @Operation(
            summary = "Find programming language by ID",
            description = "Get details of a programming language by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Programming language found",
            content = @Content(schema = @Schema(implementation = ProgrammingLanguageDto.class))
    )
    @GetMapping("/{id}")
    ProgrammingLanguageDto findById(final @PathVariable("id") String id);

    /**
     * Create a new programming language.
     *
     * @param programmingLanguageDto The programming language data to create.
     * @return ProgrammingLanguageDto object representing the newly created programming language.
     */
    @Operation(
            summary = "Create a new programming language",
            description = "Create a new programming language entry"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Programming language created",
            content = @Content(schema = @Schema(implementation = ProgrammingLanguageDto.class))
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProgrammingLanguageDto create(final @RequestBody @Validated ProgrammingLanguageDto programmingLanguageDto);

    /**
     * Update a programming language.
     *
     * @param id The ID of the programming language to update.
     * @param programmingLanguageDto The updated programming language data.
     * @return ProgrammingLanguageDto object representing the updated programming language.
     */
    @Operation(
            summary = "Update a programming language",
            description = "Update an existing programming language by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Programming language updated",
            content = @Content(schema = @Schema(implementation = ProgrammingLanguageDto.class))
    )
    @PutMapping("/{id}")
    ProgrammingLanguageDto update(
            final @PathVariable("id") String id,
            final @RequestBody @Validated ProgrammingLanguageDto programmingLanguageDto
    );

    /**
     * Delete a programming language.
     *
     * @param id The ID of the programming language to delete.
     */
    @Operation(
            summary = "Delete a programming language",
            description = "Delete a programming language by ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Programming language deleted"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(final @PathVariable("id") String id);
}