package com.example.documentstorage.shared.base.exceptions;

/**
 * The base exception in this service
 */
public abstract class ApiException extends RuntimeException {
    private final String messageKey;

    protected ApiException(String messageKey, Exception innerException) {
        super(innerException);
        this.messageKey = messageKey;
    }

    protected ApiException(String messageKey) {
        this(messageKey, null);
    }

    public String getMessageKey() {
        return messageKey;
    }
}
