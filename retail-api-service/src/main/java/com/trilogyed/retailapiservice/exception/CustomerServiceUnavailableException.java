package com.trilogyed.retailapiservice.exception;

public class CustomerServiceUnavailableException extends RuntimeException {
    public CustomerServiceUnavailableException() {
    }

    public CustomerServiceUnavailableException(String message) {
        super(message);
    }
}
