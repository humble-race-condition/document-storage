package com.example.documentstorage.config;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.errorresponse.ErrorResponse;
import com.example.documentstorage.shared.base.exceptions.ApiException;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
import com.example.documentstorage.shared.base.exceptions.InvalidSystemStateException;
import com.example.documentstorage.shared.base.problemdetail.ProblemDetailMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ProblemDetailMapper problemDetailMapper;
    private final LocalizationService localizationService;

    public GlobalExceptionHandler(ProblemDetailMapper problemDetailMapper, LocalizationService localizationService) {
        this.problemDetailMapper = problemDetailMapper;
        this.localizationService = localizationService;
    }

    //ToDo general exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        String path = Optional.ofNullable(request.getRequestURI()).orElse("");
        ErrorResponse errorResponse = new ErrorResponse(new ArrayList<>(), path, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiExceptions(ApiException ex) {
        logger.error(ex.getMessage(), ex);

        int status = switch (ex) {
            case InvalidClientInputException clientInputException -> 400;
            case InvalidSystemStateException invalidSystemStateException -> 500;
            default -> 500;
        };

        ErrorMessage errorMessage = localizationService.getErrorMessage(ex.getMessageKey(), ex.getMessageArgs());
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetailMapper.fillProblemDetails(problemDetail, errorMessage);

        return problemDetail;
    }
}
