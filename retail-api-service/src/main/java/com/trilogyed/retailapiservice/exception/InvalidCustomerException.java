package com.trilogyed.retailapiservice.exception;

public class InvalidCustomerException extends RuntimeException {
    public InvalidCustomerException() {
    }

    public InvalidCustomerException(String message) {
        super(message);
    }
}
