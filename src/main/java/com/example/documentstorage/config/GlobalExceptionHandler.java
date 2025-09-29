package com.example.documentstorage.config;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.errorresponse.ErrorCode;
import com.example.documentstorage.shared.base.errorresponse.ErrorDetails;
import com.example.documentstorage.shared.base.errorresponse.ErrorResponse;
import com.example.documentstorage.shared.base.exceptions.ApiException;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import com.example.documentstorage.shared.base.problemdetail.ProblemDetailMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ProblemDetailMapper problemDetailMapper;

    public GlobalExceptionHandler(ProblemDetailMapper problemDetailMapper) {
        this.problemDetailMapper = problemDetailMapper;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(Exception ex, HttpServletRequest request) {
        String path = Optional.ofNullable(request.getRequestURI()).orElse("");
        ErrorResponse errorResponse = new ErrorResponse(new ArrayList<>(), path, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //ToDo maybe this should return error directly, without throwing an exception
    //ToDo handle general exceptions
    //ToDo fix this - check if this handler is needed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorDetails> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(x -> new ErrorDetails(ErrorCode.INVALID_CLIENT_INPUT, x.getDefaultMessage()))
                .toList();
        String path = Optional.ofNullable(request.getRequestURI()).orElse("");
        ErrorResponse errorResponse = new ErrorResponse(errors, path, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleValidationExceptions(ApiException ex) {
        logger.error(ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        //ToDo remove errors from exception. The exception should hold only code
        problemDetail = problemDetailMapper.fillProblemDetails(problemDetail, new ErrorMessage(ex.getTitle(), ex.getDetail(), ex.getCode()), ex.getErrors());
        return problemDetail;
    }
}
