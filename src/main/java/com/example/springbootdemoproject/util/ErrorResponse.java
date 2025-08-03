package com.example.springbootdemoproject.util;


import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        List<ErrorDetails> errors,
        String path,
        LocalDateTime timestamp) {
}
