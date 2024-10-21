package com.example.rest.service.exception;

public class DuplicatedElementsException extends RuntimeException {
    public DuplicatedElementsException() {
        super("List has a duplicated elements!");
    }
}
