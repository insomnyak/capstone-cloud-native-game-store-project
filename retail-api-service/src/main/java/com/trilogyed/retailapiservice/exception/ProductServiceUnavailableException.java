package com.trilogyed.retailapiservice.exception;

public class ProductServiceUnavailableException extends RuntimeException {
    public ProductServiceUnavailableException() {
    }

    public ProductServiceUnavailableException(String message) {
        super(message);
    }
}
