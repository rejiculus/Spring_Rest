package com.example.rest.entity.exception;

/**
 * Thrown when completed param is defined but created param is not.
 */
public class CreatedNotDefinedException extends RuntimeException {
    public CreatedNotDefinedException() {
        super("Created time can't be null!");
    }
}
