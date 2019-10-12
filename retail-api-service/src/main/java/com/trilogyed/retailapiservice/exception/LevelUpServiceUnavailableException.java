package com.trilogyed.retailapiservice.exception;

public class LevelUpServiceUnavailableException extends RuntimeException {
    public LevelUpServiceUnavailableException() {
    }

    public LevelUpServiceUnavailableException(String message) {
        super(message);
    }
}
