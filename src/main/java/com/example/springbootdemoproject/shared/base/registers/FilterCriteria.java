package com.example.springbootdemoproject.shared.base.registers;

import jakarta.validation.constraints.NotBlank;

public record FilterCriteria(String @NotBlank [] filter, String operator) {
}
