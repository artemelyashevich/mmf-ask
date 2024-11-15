package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.dto.post.PostStatisticsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * API controller for managing posts.
 */
@RequestMapping("/api/v1/posts")
@Tag(name = "Post Controller", description = "APIs for managing posts")
public interface PostController {

    /**
     * Find all posts.
     *
     * @param query The search query.
     * @param page  The page number.
     * @param size  The size of each page.
     * @return Page of PostResponseDto objects representing the paginated list of posts.
     */
    @Operation(
            summary = "Find all posts",
            description = "Get a paginated list of posts"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Posts found",
            content = @Content(schema = @Schema(implementation = List.class))
    )
    @GetMapping
    Page<PostResponseDto> findAll(
            final @RequestParam(value = "q", required = false, defaultValue = "") String query,
            final @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            final @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    );

    /**
     * Find post by ID.
     *
     * @param id The ID of the post to find.
     * @return PostResponseDto object representing the post with the specified ID.
     */
    @Operation(
            summary = "Find post by ID",
            description = "Get details of a post by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post found",
            content = @Content(schema = @Schema(implementation = PostResponseDto.class))
    )
    @GetMapping("/{id}")
    PostResponseDto findById(final @PathVariable("id") String id);

    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    void like(final @PathVariable("id") String id, final @RequestParam("email") String email);

    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    void undoLike(final @PathVariable("id") String id, final @RequestParam("email") String email);

    @PostMapping("/{id}/dislike")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    void dislike(final @PathVariable("id") String id, final @RequestParam("email") String email);

    @DeleteMapping("/{id}/dislike")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    void undoDislike(final @PathVariable("id") String id, final @RequestParam("email") String email);

    /**
     * Create a new post.
     *
     * @param dto The request data for creating the post.
     * @return PostResponseDto object representing the newly created post.
     */
    @Operation(
            summary = "Create a new post",
            description = "Create a new post"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Post created",
            content = @Content(schema = @Schema(implementation = PostResponseDto.class))
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#dto.creatorEmail() == authentication.principal")
    PostResponseDto create(final @RequestBody @Validated PostRequestDto dto);

    /**
     * Upload image for a post.
     *
     * @param id    The ID of the post for which images are being uploaded.
     * @param files The image files to upload.
     * @param email The email of the user uploading the images.
     * @return PostResponseDto object representing the post with the uploaded images.
     * @throws Exception if an error occurs during the image upload process.
     */
    @Operation(
            summary = "Upload image for a post",
            description = "Upload image(s) for a post"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Image uploaded for post",
            content = @Content(schema = @Schema(implementation = PostResponseDto.class))
    )
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    PostResponseDto uploadImage(
            final @PathVariable("id") String id,
            final @RequestParam("files") MultipartFile[] files,
            final @RequestParam("email") String email
    ) throws Exception;

    /**
     * Update a post.
     *
     * @param id    The ID of the post to update.
     * @param dto   The updated post data.
     * @param email The email of the user updating the post.
     * @return PostResponseDto object representing the updated post.
     */
    @Operation(
            summary = "Update a post",
            description = "Update an existing post by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post updated",
            content = @Content(schema = @Schema(implementation = PostResponseDto.class))
    )
    @PutMapping("/{id}")
    @PreAuthorize("#email == authentication.principal")
    PostResponseDto update(
            final @PathVariable("id") String id,
            final @RequestBody @Validated PostRequestDto dto,
            final @RequestParam("email") String email
    );

    /**
     * Delete a post.
     *
     * @param id    The ID of the post to delete.
     * @param email The email of the user deleting the post.
     */
    @Operation(
            summary = "Delete a post",
            description = "Delete a post by ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Post deleted"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#email == authentication.principal")
    void delete(final @PathVariable("id") String id, final @RequestParam("email") String email);

    @GetMapping("/statistics")
    PostStatisticsDto findStatistics();
}