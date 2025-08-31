package com.example.springbootdemoproject.shared.base.exceptions;

/**
 * Exception thrown when the system is not in a valid state
 */
public class InvalidSystemStateException extends ApiException {
    private static final int STATUS = 500;

    public InvalidSystemStateException(ErrorMessage errorMessage, Exception innerException) {
        super(STATUS, errorMessage, null, innerException);
    }
}
