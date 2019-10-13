package com.trilogyed.retailapiservice.exception;

public class InventoryServiceUnavailableException extends RuntimeException {
    public InventoryServiceUnavailableException() {
    }

    public InventoryServiceUnavailableException(String message) {
        super(message);
    }
}
