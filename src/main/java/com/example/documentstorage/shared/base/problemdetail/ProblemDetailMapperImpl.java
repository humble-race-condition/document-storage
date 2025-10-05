package com.example.documentstorage.shared.base.problemdetail;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProblemDetailMapperImpl implements ProblemDetailMapper {
    private static final HttpStatus STATUS_CODE = HttpStatus.BAD_REQUEST;
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
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        ErrorMessage errorMessage = localizationService.getErrorMessage("validation.error.message");

        ProblemDetail problemDetail = generateProblemDetails(STATUS_CODE, errorMessage, errors);

        ResponseEntity<ProblemDetail> response = ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
        return response;
    }

    @Override
    public ProblemDetail generateProblemDetails(HttpStatus status, ErrorMessage errorMessage, List<String> errors) {
        ProblemDetail problemDetail = generateProblemDetail(status, errorMessage, errors);
        return problemDetail;
    }

    @Override
    public ProblemDetail generateProblemDetails(HttpStatus status, ErrorMessage errorMessage) {
        ProblemDetail problemDetail = generateProblemDetail(status, errorMessage, List.of());
        return problemDetail;
    }

    private static ProblemDetail generateProblemDetail(HttpStatus status, ErrorMessage errorMessage, List<String> errors) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(errorMessage.title());
        problemDetail.setDetail(errorMessage.detail());
        problemDetail.setProperty("code", errorMessage.code());
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}
