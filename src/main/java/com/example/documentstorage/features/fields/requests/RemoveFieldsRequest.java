package com.example.documentstorage.features.fields.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RemoveFieldsRequest(
        @NotNull
        List<@NotBlank String> fields) {
}
