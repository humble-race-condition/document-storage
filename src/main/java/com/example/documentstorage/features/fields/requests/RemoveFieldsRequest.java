package com.example.documentstorage.features.fields.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RemoveFieldsRequest(
        @NotNull(message = "{features.fields.requests.remove.fields.invalid}")
        List<@NotBlank(message = "{features.fields.requests.remove.fields.name.empty}") String> fields) {
}
