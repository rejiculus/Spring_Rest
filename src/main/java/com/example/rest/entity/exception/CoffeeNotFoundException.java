package com.example.rest.entity.exception;

import java.util.List;

/**
 * Thrown when Coffee entity is not found in db
 */
public class CoffeeNotFoundException extends RuntimeException {
    public CoffeeNotFoundException(Long id) {
        super(String.format("Coffee '%d' is not found!", id));
    }

    public CoffeeNotFoundException(List<Long> idList) {
        super(String.format("Not found coffees: %s", idList));
    }
}
