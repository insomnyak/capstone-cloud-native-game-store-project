package com.trilogyed.retailapiservice.exception;

public class InvoiceServiceUnavailableException extends RuntimeException {
    public InvoiceServiceUnavailableException() {
    }

    public InvoiceServiceUnavailableException(String message) {
        super(message);
    }
}
