package com.example.springbootdemoproject.shared.base.apimessages;

import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
import io.micrometer.common.lang.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationServiceImpl implements LocalizationService {
    private final MessageSource messageSource;

    public LocalizationServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Base method for retrieving a localized message
     *
     * @param code the code for the message
     * @param args arguments for formatting the string
     * @return the string from the message
     */
    @Override
    public String getMessage(String code, @Nullable Object[]... args) {
        String message = messageSource.getMessage(code, args, Locale.ROOT);
        return message;
    }

    /**
     * Gets the error message title, details and code in a single object
     *
     * @param code the code for the title is {@code code}. This serves as a base message. The code for the detail
     *             is {@code code.detail} and code for the error code is {@code code.code}
     * @param args arguments for formatting the string
     * @return error message object containing the different error messages
     */
    @Override
    public ErrorMessage getErrorMessage(String code, @Nullable Object... args) {
        String title = messageSource.getMessage(code, args, Locale.ROOT);
        String detail = messageSource.getMessage(code + ".detail", args, Locale.ROOT);
        String errorCode = messageSource.getMessage(code + ".code", args, Locale.ROOT);
        return new ErrorMessage(title, detail, errorCode);
    }
}
