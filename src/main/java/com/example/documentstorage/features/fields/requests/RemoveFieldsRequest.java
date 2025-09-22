package com.example.documentstorage.features.fields.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RemoveFieldsRequest(
        @NotNull
        @Valid
        List<String> fields) {
}
