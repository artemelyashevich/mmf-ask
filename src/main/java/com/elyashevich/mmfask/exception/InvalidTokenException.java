package com.elyashevich.mmfask.exception;

/**
 * Custom exception for invalid tokens.
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * Constructs an InvalidTokenException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
