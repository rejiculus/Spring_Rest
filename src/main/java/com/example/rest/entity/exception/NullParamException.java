package com.example.rest.entity.exception;

/**
 * Thrown when some params is null.
 */
public class NullParamException extends RuntimeException {
    public NullParamException() {
        super("Parameter cannot be null!");
    }
}
