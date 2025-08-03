package com.example.springbootdemoproject.util;

public enum ErrorCode {
    INVALID_CLIENT_INPUT("1000");


    private final String code;
    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
