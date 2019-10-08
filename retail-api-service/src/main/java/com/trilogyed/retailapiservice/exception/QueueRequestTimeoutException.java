package com.trilogyed.retailapiservice.exception;

public class QueueRequestTimeoutException extends RuntimeException {
    public QueueRequestTimeoutException() {
    }

    public QueueRequestTimeoutException(String message) {
        super(message);
    }
}
