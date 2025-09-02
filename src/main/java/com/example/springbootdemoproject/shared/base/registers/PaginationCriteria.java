package com.example.springbootdemoproject.shared.base.registers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record PaginationCriteria(@PositiveOrZero int page, @PositiveOrZero int size, String @NotBlank [] sort) {
}
