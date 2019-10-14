package com.trilogyed.retailapiservice.exception;

public class UnsupportedDomainReturnType extends RuntimeException {
    public UnsupportedDomainReturnType() {
    }

    public UnsupportedDomainReturnType(String message) {
        super(message);
    }
}
