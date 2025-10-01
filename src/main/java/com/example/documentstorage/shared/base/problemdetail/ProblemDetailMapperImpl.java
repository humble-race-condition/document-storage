package com.example.documentstorage.shared.base.problemdetail;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemDetailMapperImpl implements ProblemDetailMapper {
    private static final int STATUS_CODE = 400;
    private static final Logger logger = LoggerFactory.getLogger(ProblemDetailMapperImpl.class);
    private final LocalizationService localizationService;

    public ProblemDetailMapperImpl(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public ResponseEntity<ProblemDetail> handleBindingResult(BindingResult result) {
        String typeName = Optional.ofNullable(result.getTarget())
                .map(Object::getClass)
                .map(Class::getName)
                .orElse("");

        logger.warn("Validation failed for type '{}'", typeName);
        List<String> errors = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorMessage errorMessage = localizationService.getErrorMessage("validation.error.message");

        ProblemDetail problemDetail = ProblemDetail.forStatus(STATUS_CODE);
        fillProblemDetails(problemDetail, errorMessage, errors);

        ResponseEntity<ProblemDetail> response = ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
        return response;
    }

    @Override
    public void fillProblemDetails(ProblemDetail problemDetail, ErrorMessage errorMessage, String... errors)  {
        fillBaseFields(problemDetail, errorMessage);
        problemDetail.setProperty("errors", errors);
    }

    @Override
    public void fillProblemDetails(ProblemDetail problemDetail, ErrorMessage errorMessage, List<String> errors)  {
        fillBaseFields(problemDetail, errorMessage);
        problemDetail.setProperty("errors", errors);
    }

    private static void fillBaseFields(ProblemDetail problemDetail, ErrorMessage errorMessage) {
        problemDetail.setProperty("code", errorMessage.code());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errors", "ex.getErrors()");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
    }
}
