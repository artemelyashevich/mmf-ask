package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.api.dto.user.UserStatisticsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * API controller for managing users.
 */
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "APIs for user management")
public interface UserController {

    /**
     * Find all users.
     *
     * @return List of UserDto objects representing all users.
     */
    @Operation(summary = "Find all users", description = "Get a list of all users")
    @ApiResponse(
            responseCode = "200",
            description = "Users found",
            content = @Content(schema = @Schema(implementation = List.class))
    )
    @GetMapping
    Page<UserDto> findAll(
            @RequestParam(name = "q", required = false, defaultValue = "") String searchValue,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(name = "sortField", required = false, defaultValue = "createdAt") String sortField
    );

    @Operation(summary = "Find current user", description = "Get user from jwt claims")
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @GetMapping("/find/current")
    UserDto findCurrent();

    /**
     * Find user by ID.
     *
     * @param id The ID of the user to find.
     * @return UserDto object representing the user with the specified ID.
     */
    @Operation(
            summary = "Find user by ID",
            description = "Get user details by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @GetMapping("/{id}")
    UserDto findById(final @PathVariable("id") String id);

    @Operation(
            summary = "Find user statistics",
            description = "Get user statistics"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User statistics found",
            content = @Content(schema = @Schema(implementation = UserStatisticsDto.class))
    )
    @GetMapping("/statistics")
    UserStatisticsDto findStatistics();

    /**
     * Upload user image.
     *
     * @param email The email of the user to upload the image for.
     * @param file  The image file to upload.
     * @return UserDto object representing the user with the uploaded image.
     * @throws Exception if an error occurs during the image upload process.
     */
    @Operation(
            summary = "Upload user image",
            description = "Upload image for a user by email"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Image uploaded successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @PostMapping("/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    UserDto uploadImage(
            final @PathVariable("email") String email,
            final @RequestParam("file") MultipartFile file
    ) throws Exception;

    @Operation(
            summary = "Update user email",
            description = "Update user email"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @PutMapping
    UserDto updateUser(@RequestBody UserDto userDto) throws MessagingException;

    @Operation(
            summary = "Update user email",
            description = "Update user email"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @PutMapping("/{code}")
    UserDto updateEmailUser(@PathVariable("code") String code, @RequestBody UserDto userDto) throws MessagingException;
}
