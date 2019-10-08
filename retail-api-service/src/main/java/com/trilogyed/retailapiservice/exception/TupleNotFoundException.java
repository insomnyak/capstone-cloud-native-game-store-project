package com.trilogyed.retailapiservice.exception;

public class TupleNotFoundException extends RuntimeException {
    public TupleNotFoundException() {
    }

    public TupleNotFoundException(String message) {
        super(message);
    }
}
