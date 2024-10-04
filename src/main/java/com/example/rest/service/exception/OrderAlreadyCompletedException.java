package com.example.rest.service.exception;

import com.example.rest.entity.Order;

/**
 * Thrown when trying to complete order that already completed.
 */
public class OrderAlreadyCompletedException extends RuntimeException {
    public OrderAlreadyCompletedException(Order order) {
        super(String.format("Order '%d' is already completed at '%s'!", order.getId(), order.getCompleted()));
    }
}
