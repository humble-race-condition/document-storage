package com.example.springbootdemoproject.config;

import com.example.springbootdemoproject.shared.errorresponse.ErrorCode;
import com.example.springbootdemoproject.shared.errorresponse.ErrorDetails;
import com.example.springbootdemoproject.shared.errorresponse.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorDetails> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(x -> new ErrorDetails(ErrorCode.INVALID_CLIENT_INPUT, x.getDefaultMessage()))
                .toList();
        String path = Optional.ofNullable(request.getRequestURI()).orElse("");
        ErrorResponse errorResponse = new ErrorResponse(errors, path, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
