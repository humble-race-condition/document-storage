package com.example.springbootdemoproject.shared.errorresponse;


import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        List<ErrorDetails> errors,
        String path,
        LocalDateTime timestamp) {
}
