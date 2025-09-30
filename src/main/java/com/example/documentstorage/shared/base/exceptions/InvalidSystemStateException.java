package com.example.documentstorage.shared.base.exceptions;

/**
 * Exception thrown when the system is not in a valid state
 */
public class InvalidSystemStateException extends ApiException {
    protected InvalidSystemStateException(String messageKey) {
        super(messageKey);
    }
}
