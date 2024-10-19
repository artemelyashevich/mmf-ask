package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller for managing favorites.
 */
@RequestMapping("/api/v1/favorites")
@Tag(name = "Favorites Controller", description = "APIs for managing favorites")
public interface FavoritesController {

    /**
     * Find all favorites.
     *
     * @return List of FavoritesDto objects representing all favorites.
     */
    @Operation(
            summary = "Find all favorites",
            description = "Get a list of all favorites"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Favorites found",
            content = @Content(schema = @Schema(implementation = List.class))
    )
    @GetMapping
    List<FavoritesDto> findAll();

    /**
     * Find favorites by user email.
     *
     * @param email The email of the user.
     * @return FavoritesDto object representing the favorites of the user.
     */
    @Operation(
            summary = "Find favorites by user email",
            description = "Get favorites by user email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Favorites found",
            content = @Content(schema = @Schema(implementation = FavoritesDto.class))
    )
    @GetMapping("/user/{userEmail}")
    FavoritesDto findByUserEmail(final @PathVariable("userEmail") String email);

    /**
     * Add a post to favorites.
     *
     * @param postId The ID of the post to add to favorites.
     * @param email The email of the user adding the post to favorites.
     * @return FavoritesDto object representing the updated favorites.
     */
    @Operation(
            summary = "Add a post to favorites",
            description = "Add a post to user favorites"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Post added to favorites",
            content = @Content(schema = @Schema(implementation = FavoritesDto.class))
    )
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    FavoritesDto addPost(
            final @PathVariable("postId") String postId,
            final @RequestParam("email") String email
    );

    /**
     * Remove a post from favorites.
     *
     * @param postId The ID of the post to remove from favorites.
     * @param email The email of the user removing the post from favorites.
     * @return FavoritesDto object representing the updated favorites after removal.
     */
    @Operation(
            summary = "Remove a post from favorites",
            description = "Remove a post from user favorites"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post removed from favorites",
            content = @Content(schema = @Schema(implementation = FavoritesDto.class))
    )
    @PutMapping("/{postId}")
    @PreAuthorize("#email == authentication.principal")
    FavoritesDto removePost(
            final @PathVariable("postId") String postId,
            final @RequestParam("email") String email
    );

    /**
     * Delete user favorites.
     *
     * @param email The email of the user whose favorites are to be deleted.
     */
    @Operation(
            summary = "Delete user favorites",
            description = "Delete all favorites of a user"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Favorites deleted"
    )
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#email == authentication.principal")
    void delete(final @RequestParam("email") String email);
}