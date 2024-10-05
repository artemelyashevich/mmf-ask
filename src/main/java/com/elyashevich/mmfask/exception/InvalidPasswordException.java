package com.elyashevich.mmfask.exception;

/**
 * Custom exception for invalid passwords.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructs an InvalidPasswordException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
