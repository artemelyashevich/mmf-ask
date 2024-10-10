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

    String activateUser(final String email, final String code);

    void sendResetPasswordCode(final String email) throws MessagingException;

    String resetPassword(final String email, final String code, final ResetPasswordDto dto);
}
