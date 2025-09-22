package com.example.documentstorage.shared.base.exceptions;

/**
 * Interface implementing additional api errors when they occur
 */
public interface ApiExceptionError {
    /**
     * @return gets the code of the error
     */
    String getCode();

    /**
     * @return gets the title of the error
     */
    String getTitle();

    /**
     * @return gets the detail of the error
     */
    String getDetail();
}
