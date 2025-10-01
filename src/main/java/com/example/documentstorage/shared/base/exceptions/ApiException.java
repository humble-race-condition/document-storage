package com.example.documentstorage.shared.base.exceptions;

/**
 * The base exception in this service
 */
public abstract class ApiException extends RuntimeException {
    private final String messageKey;
    private final Object[] messageArgs;

    protected ApiException(String messageKey, Exception innerException, Object... messageArgs) {
        super(innerException);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    protected ApiException(String messageKey, Object... messageArgs) {
        this(messageKey, null, messageArgs);
    }

    protected ApiException(String messageKey) {
        this(messageKey, null, new Object[0]);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }
}
