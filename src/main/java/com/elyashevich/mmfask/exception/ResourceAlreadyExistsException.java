package com.elyashevich.mmfask.exception;

/**
 * Custom exception for resource already existing.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException() {
    }

    /**
     * Constructs a ResourceAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
