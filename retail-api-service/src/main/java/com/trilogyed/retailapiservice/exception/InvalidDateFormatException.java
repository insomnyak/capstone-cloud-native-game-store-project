package com.trilogyed.retailapiservice.exception;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException() {
    }

    public InvalidDateFormatException(String message) {
        super(message);
    }
}
