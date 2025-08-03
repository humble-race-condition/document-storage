package com.example.springbootdemoproject.util;


import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        List<ErrorDetails> errorInfo,
        String path,
        LocalDateTime timestamp
        ) {}
