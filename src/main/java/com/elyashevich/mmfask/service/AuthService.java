package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    /**
     * Registers a user with the provided authentication request.
     *
     * @param authRequestDto the authentication request DTO containing user information
     * @return a message indicating the registration status
     */
    String register(final AuthRequestDto authRequestDto);

    /**
     * Logs in a user with the provided authentication request.
     *
     * @param authRequestDto the authentication request DTO containing user information
     * @return a message indicating the login status
     */
    String login(final AuthRequestDto authRequestDto);
}
