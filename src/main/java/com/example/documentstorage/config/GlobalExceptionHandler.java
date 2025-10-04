package com.example.documentstorage.config;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.exceptions.ApiException;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
import com.example.documentstorage.shared.base.exceptions.InvalidSystemStateException;
import com.example.documentstorage.shared.base.problemdetail.ProblemDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ProblemDetailMapper problemDetailMapper;
    private final LocalizationService localizationService;

    public GlobalExceptionHandler(ProblemDetailMapper problemDetailMapper, LocalizationService localizationService) {
        this.problemDetailMapper = problemDetailMapper;
        this.localizationService = localizationService;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        logger.error("An unexpected error occurred", ex);
        ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");

        ProblemDetail problemDetail = problemDetailMapper.generateProblemDetails(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);

        return problemDetail;
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiExceptions(ApiException ex) {
        ErrorMessage errorMessage = localizationService.getErrorMessage(ex.getMessageKey(), ex.getMessageArgs());

        HttpStatus status = switch (ex) {
            case InvalidClientInputException clientInputException -> {
                logger.warn(errorMessage.detail(), clientInputException);
                yield HttpStatus.BAD_REQUEST;
            }
            case InvalidSystemStateException invalidSystemStateException -> {
                logger.error(errorMessage.detail(), invalidSystemStateException);
                yield HttpStatus.INTERNAL_SERVER_ERROR;
            }
            default -> {
                logger.error(ex.getMessage(), ex);
                yield HttpStatus.INTERNAL_SERVER_ERROR;
            }
        };

        ProblemDetail problemDetail = problemDetailMapper.generateProblemDetails(status, errorMessage);

        return problemDetail;
    }
}
