package com.example.documentstorage.shared.base.exceptions;

/**
 * Exception thrown when the system is not in a valid state
 */
public class InvalidSystemStateException extends ApiException {
    private static final int STATUS = 500;

    //ToDo this exception always accepts a message that is a general error. Instead, handle these and load the message
    //in the controller advice. Do not pass it trough the constructor
    public InvalidSystemStateException(ErrorMessage errorMessage, Exception innerException) {
        super(STATUS, errorMessage, null, innerException);
    }
}
