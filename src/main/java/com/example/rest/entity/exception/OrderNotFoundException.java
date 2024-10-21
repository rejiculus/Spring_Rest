package com.example.rest.entity.exception;

import java.util.List;

/**
 * Thrown when Order entity is not found in db.
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super(String.format("Order '%d' is not found!", id));
    }

    public OrderNotFoundException(List<Long> idList) {
        super(String.format("Not found orders: %s", idList));
    }
}
