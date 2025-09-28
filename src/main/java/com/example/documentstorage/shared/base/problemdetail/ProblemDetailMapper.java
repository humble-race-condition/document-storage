package com.example.documentstorage.shared.base.problemdetail;

import com.example.documentstorage.shared.base.exceptions.ApiExceptionError;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ProblemDetailMapper {
    ProblemDetail mapToProblemDetail(BindingResult result);

    ProblemDetail fillProblemDetails(ProblemDetail problemDetail, ErrorMessage errorMessage, List<ApiExceptionError> errors);
}
