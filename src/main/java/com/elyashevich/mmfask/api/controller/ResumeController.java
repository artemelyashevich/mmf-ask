package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.resume.ResumeCreateDto;
import com.elyashevich.mmfask.api.dto.resume.ResumeResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/v1/resumes")
@Tag(name = "Resume Management", description = "API for managing user resumes")
public interface ResumeController {

    @GetMapping("/{id}")
    @Operation(
            summary = "Get resume by ID",
            description = "Retrieves a specific resume by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resume found"),
                    @ApiResponse(responseCode = "404", description = "Resume not found")
            }
    )
    ResumeResponseDto findById(
            @Parameter(description = "ID of the resume to retrieve", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable("id") String id
    );

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get resume by user ID",
            description = "Retrieves a resume associated with a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resume found"),
                    @ApiResponse(responseCode = "404", description = "Resume not found")
            }
    )
    ResumeResponseDto findByUserId(
            @Parameter(description = "ID of the user whose resume to retrieve", required = true, example = "6587a8e6a3f12b3c4d5e6f7a")
            @PathVariable("userId") String userId
    );

    @PostMapping
    @Operation(
            summary = "Create a new resume",
            description = "Creates a new resume with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resume successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    ResumeResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Resume creation data",
                    required = true
            )
            @Valid @RequestBody ResumeCreateDto dto
    );

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a resume",
            description = "Updates an existing resume with new data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resume successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Resume not found")
            }
    )
    ResumeResponseDto update(
            @Parameter(description = "ID of the resume to update", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable("id") String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated resume data",
                    required = true
            )
            @Valid @RequestBody ResumeCreateDto dto
    );

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a resume",
            description = "Deletes a specific resume by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resume successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Resume not found")
            }
    )
    void delete(
            @Parameter(description = "ID of the resume to delete", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable("id") String id
    );
}