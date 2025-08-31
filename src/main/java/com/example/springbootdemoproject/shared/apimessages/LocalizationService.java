package com.example.springbootdemoproject.shared.apimessages;

import com.example.springbootdemoproject.shared.exceptions.ErrorMessage;
import io.micrometer.common.lang.Nullable;

public interface LocalizationService {
    /**
     * Base method for retrieving a localized message
     *
     * @param code the code for the message
     * @param args arguments for formatting the string
     * @return the string from the message
     */
    String getMessage(String code, @Nullable Object[]... args);

    /**
     * Gets the error message title, details and code in a single object
     * @param code the code for the title is {@code code.title}. This serves as a base message. The code for the detail
     *             is {@code code.detail} and code for the error code is {@code code.code}
     * @param args arguments for formatting the string
     * @return error message object containing the different error messages
     */
    ErrorMessage getErrorMessage(String code, @Nullable Object... args);
}
