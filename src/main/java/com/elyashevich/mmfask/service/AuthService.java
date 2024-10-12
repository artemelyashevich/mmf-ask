package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import jakarta.mail.MessagingException;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    /**
     * Registers a user with the provided authentication request.
     *
     * @param authRequestDto the authentication request DTO containing user information
     */
    void register(final AuthRequestDto authRequestDto) throws MessagingException;

    /**
     * Logs in a user with the provided authentication request.
     *
     * @param authRequestDto the authentication request DTO containing user information
     * @return a message indicating the login status
     */
    String login(final AuthRequestDto authRequestDto);

    /**
     * Activates a user with the given email using the provided code.
     *
     * @param email The email of the user to activate.
     * @param code The activation code.
     * @return A message indicating the result of the activation process.
     */
    String activateUser(final String email, final String code);

    /**
     * Sends a reset password code to the specified email address.
     *
     * @param email The email address of the user requesting the reset code.
     * @throws MessagingException If an error occurs while sending the reset code.
     */
    void sendResetPasswordCode(final String email) throws MessagingException;

    /**
     * Resets the password for a user with the provided email, code, and new password details.
     *
     * @param email The email of the user for password reset.
     * @param code The reset password code.
     * @param dto The data for resetting the password.
     * @return A message indicating the result of the password reset process.
     */
    String resetPassword(final String email, final String code, final ResetPasswordDto dto);
}
