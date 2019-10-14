package com.trilogyed.retailapiservice.exception;

public class UnsupportedHttpStatusException extends RuntimeException {
    public UnsupportedHttpStatusException() {
    }

    public UnsupportedHttpStatusException(String message) {
        super(message);
    }
}
