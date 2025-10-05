package com.example.documentstorage.shared.base.models.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record FieldInfo(
        @NotBlank(message = "{shared.base.models.requests.field.info.name.empty}")
        @Length(min = 2, max = 20, message = "{shared.base.models.requests.field.info.name.invalid.length}")
        String name,
        @NotBlank(message = "{shared.base.models.requests.field.info.value.empty}")
        @Length(min = 2, max = 20, message = "{shared.base.models.requests.field.info.value.invalid.length}")
        String value) {
}
