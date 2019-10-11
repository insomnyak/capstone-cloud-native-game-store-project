package com.trilogyed.retailapiservice.exception;

public class ProductUnavailableException extends RuntimeException {
    public ProductUnavailableException() {
    }

    public ProductUnavailableException(String message) {
        super(message);
    }
}
