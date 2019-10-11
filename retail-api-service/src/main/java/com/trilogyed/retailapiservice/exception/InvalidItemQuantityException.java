package com.trilogyed.retailapiservice.exception;

public class InvalidItemQuantityException extends RuntimeException {
    public InvalidItemQuantityException() {
    }

    public InvalidItemQuantityException(String message) {
        super(message);
    }
}
