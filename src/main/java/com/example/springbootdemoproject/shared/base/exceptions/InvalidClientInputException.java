package com.example.springbootdemoproject.shared.base.exceptions;

/**
 * Exception thrown when the client input is not valid
 */
public class InvalidClientInputException extends ApiException {
    private static final int STATUS = 400;

    public InvalidClientInputException(ErrorMessage errorMessage) {
        super(STATUS, errorMessage, null, null);
    }
}
