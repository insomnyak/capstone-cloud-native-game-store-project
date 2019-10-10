package com.trilogyed.adminapi.exception;

public class CannotCreateInvoice extends RuntimeException {

    public CannotCreateInvoice() {
    }

    public CannotCreateInvoice(String message) {
        super(message);
    }
}
