package com.example.documentstorage.shared.base.exceptions;

/**
 * Exception thrown when the client input is not valid
 */
public class InvalidClientInputException extends ApiException {
    public InvalidClientInputException(String messageKey, Object... messageArgs) {
        super(messageKey, messageArgs);
    }
}
