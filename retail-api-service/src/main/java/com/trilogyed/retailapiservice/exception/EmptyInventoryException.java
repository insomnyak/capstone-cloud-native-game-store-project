package com.trilogyed.retailapiservice.exception;

public class EmptyInventoryException extends RuntimeException {
    public EmptyInventoryException() {
    }

    public EmptyInventoryException(String message) {
        super(message);
    }
}
