package com.example.documentstorage.shared.base.apimessages;

import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import io.micrometer.common.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationServiceImpl implements LocalizationService {
    private static final Logger logger = LoggerFactory.getLogger(LocalizationServiceImpl.class);
    private static final String DEFAULT_MESSAGE = "";

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
    public String getMessage(String code, @Nullable Object... args) {
        String message = messageSource.getMessage(code, args, null, Locale.ROOT);
        if (message == null) {
            logger.warn("No message found for code '{}'", code);
            return DEFAULT_MESSAGE;
        }

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
