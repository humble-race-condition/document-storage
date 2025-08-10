package com.example.springbootdemoproject.shared.base.models.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record FieldInfo(
        @NotBlank(message = "The field name must not be blank")
        @Length(min = 2, max = 20, message = "The field name must be between {min} and {max}")
        String name,
        @NotBlank(message = "The field value must not be blank")
        @Length(min = 2, max = 20, message = "The field value must be between {min} and {max}")
        String value) {
}
