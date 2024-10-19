package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.api.dto.comment.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller for managing comments.
 */
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment Controller", description = "APIs for managing comments")
public interface CommentController {

    /**
     * Find all comments based on postId and/or userId.
     *
     * @param postId The ID of the post to filter comments by.
     * @param userId The ID of the user to filter comments by.
     * @return List of CommentResponseDto objects representing comments.
     */
    @Operation(
            summary = "Find all comments",
            description = "Get a list of comments based on postId and/or userId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments found",
            content = @Content(schema = @Schema(implementation = List.class))
    )
    @GetMapping
    List<CommentResponseDto> findAll(
            final @RequestParam(name = "postId", defaultValue = "", required = false) String postId,
            final @RequestParam(name = "userId", defaultValue = "", required = false) String userId
    );

    /**
     * Find a comment by its ID.
     *
     * @param id The ID of the comment to retrieve.
     * @return CommentResponseDto object representing the comment.
     */
    @Operation(
            summary = "Find comment by ID",
            description = "Get details of a comment by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment found",
            content = @Content(schema = @Schema(implementation = CommentResponseDto.class))
    )
    @GetMapping("/{commentId}")
    CommentResponseDto findById(final @PathVariable("commentId") String id);

    /**
     * Create a new comment.
     *
     * @param dto The CommentRequestDto containing the new comment data.
     * @param email The email of the user creating the comment.
     * @return CommentResponseDto object representing the created comment.
     */
    @Operation(
            summary = "Create a new comment",
            description = "Create a new comment"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Comment created",
            content = @Content(schema = @Schema(implementation = CommentResponseDto.class))
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    CommentResponseDto create(
            final @Validated @RequestBody CommentRequestDto dto,
            final @RequestParam("email") String email
    );

    /**
     * Update an existing comment by its ID.
     *
     * @param id The ID of the comment to update.
     * @param dto The updated CommentRequestDto data.
     * @param email The email of the user updating the comment.
     * @return CommentResponseDto object representing the updated comment.
     */
    @Operation(
            summary = "Update a comment",
            description = "Update an existing comment by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment updated",
            content = @Content(schema = @Schema(implementation = CommentResponseDto.class))
    )
    @PutMapping("/{commentId}")
    @PreAuthorize("#email == authentication.principal")
    CommentResponseDto update(
            final @PathVariable("commentId") String id,
            final @Validated @RequestBody CommentRequestDto dto,
            final @RequestParam("email") String email
    );

    /**
     * Delete a comment by its ID.
     *
     * @param id The ID of the comment to delete.
     * @param email The email of the user deleting the comment.
     */
    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment by ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Comment deleted",
            content = @Content(schema = @Schema(implementation = CommentResponseDto.class))
    )
    @DeleteMapping("/{commentId}")
    @PreAuthorize("#email == authentication.principal")
    void delete(final @PathVariable("commentId") String id, final @RequestParam("email") String email);
}