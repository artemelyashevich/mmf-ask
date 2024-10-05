package com.elyashevich.mmfask.exception;

/**
 * Custom exception for resource not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}