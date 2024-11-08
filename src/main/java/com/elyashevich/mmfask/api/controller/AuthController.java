package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.auth.ActivationDto;
import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.AuthResponseDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller for user authentication operations.
 */
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "APIs for user authentication")
public interface AuthController {

    /**
     * Register a new user.
     *
     * @param dto The email user for registration and activation code.
     * @throws MessagingException If an error occurs during message sending.
     */
    @Operation(
            summary = "Trying to activate a new user",
            description = "Activation a new user"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Activation code sent"
    )
    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.CREATED)
    void activate(final @Validated @RequestBody ActivationDto dto) throws MessagingException;

    /**
     * Activate a user account with email verification code.
     *
     * @param email The information of the user to activate.
     * @param code The verification code for activation.
     */
    @Operation(
            summary = "Activate user account",
            description = "Activate user account with email verification code"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User activated",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
    )
    @PostMapping("/activate/{email}")
    void activation(
            final @PathVariable("email") String email, final @RequestParam("code") String code
    );

    @PostMapping("/register/{email}")
    AuthResponseDto register(
            final @PathVariable("email") String email, final @Validated @RequestBody AuthRequestDto dto
    );

    /**
     * Login a user and generate an authentication token.
     *
     * @param authRequestDto The AuthRequestDto containing user login data.
     * @return AuthResponseDto object containing authentication information.
     */
    @Operation(
            summary = "User login",
            description = "Login user and generate authentication token"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User logged in",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    AuthResponseDto login(final @Validated @RequestBody AuthRequestDto authRequestDto);

    /**
     * Send a reset password code to the user's email.
     *
     * @param principal The principal user requesting the password reset.
     * @throws MessagingException If an error occurs during message sending.
     */
    @Operation(
            summary = "Send reset password code",
            description = "Send reset password code to user's email"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Code sent"
    )
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.CREATED)
    void resetPasswordCode(final Principal principal) throws MessagingException;

    /**
     * Reset a user's password using the provided reset code.
     *
     * @param code The reset code for password reset.
     * @param dto The ResetPasswordDto containing the new password.
     * @param principal The principal user requesting the password reset.
     * @return AuthResponseDto object representing the reset password action.
     */
    @Operation(
            summary = "Reset user password",
            description = "Reset user password using the reset code"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Code sent",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
    )
    @PostMapping("/reset-password/{code}")
    AuthResponseDto resetPassword(
            final @PathVariable("code") String code,
            final @Validated @RequestBody ResetPasswordDto dto,
            final Principal principal
    );
}