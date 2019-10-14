package com.trilogyed.retailapiservice.exception;

public class MicroserviceUnavailableException extends RuntimeException {
    public MicroserviceUnavailableException() {
    }

    public MicroserviceUnavailableException(String message) {
        super(message);
    }
}
