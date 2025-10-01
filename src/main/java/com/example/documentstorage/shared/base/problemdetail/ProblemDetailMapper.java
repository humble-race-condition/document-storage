package com.example.documentstorage.shared.base.problemdetail;

import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ProblemDetailMapper {
    ResponseEntity<ProblemDetail> handleBindingResult(BindingResult result);

    ProblemDetail generateProblemDetails(HttpStatus status, ErrorMessage errorMessage, List<String> errors);

    ProblemDetail generateProblemDetails(HttpStatus status, ErrorMessage errorMessage);
}
