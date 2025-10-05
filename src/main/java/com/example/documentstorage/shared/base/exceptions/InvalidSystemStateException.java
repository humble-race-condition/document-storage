package com.example.documentstorage.shared.base.exceptions;

/**
 * Exception thrown when the system is not in a valid state
 */
public class InvalidSystemStateException extends ApiException {
    public InvalidSystemStateException() {
        super("default.error.message");
    }

    public InvalidSystemStateException(Exception cause) {
        super("default.error.message", cause);
    }
}
