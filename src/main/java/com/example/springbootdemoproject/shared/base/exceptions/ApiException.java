package com.example.springbootdemoproject.shared.base.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The base exception in this service
 */
public abstract class ApiException extends RuntimeException {
    private final int status;
    private final String title;
    private final String detail;
    private final String code;
    private final List<ApiExceptionError> errors;

    protected ApiException(int status,
                           String title,
                           String detail,
                           String code,
                           List<ApiExceptionError> errors,
                           Exception innerException) {
        super(innerException);
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.code = code;
        this.errors = Optional.ofNullable(errors).orElse(new ArrayList<>());
    }

    protected ApiException(int status, ErrorMessage errorMessage, List<ApiExceptionError> errors, Exception innerException) {
        this(status, errorMessage.title(), errorMessage.detail(), errorMessage.code(), errors, innerException);
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getCode() {
        return code;
    }

    public List<ApiExceptionError> getErrors() {
        return errors;
    }
}
