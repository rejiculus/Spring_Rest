package com.example.rest.entity.exception;

import java.time.LocalDateTime;

/**
 * Thrown when completed time is before created time.
 */
public class CompletedBeforeCreatedException extends RuntimeException {
    public CompletedBeforeCreatedException(LocalDateTime created, LocalDateTime completed) {
        super(String.format("Complete '%s' time can't be before create time '%s'!", completed, created));
    }
}
