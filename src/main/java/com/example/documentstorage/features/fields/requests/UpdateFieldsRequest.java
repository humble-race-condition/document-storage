package com.example.documentstorage.features.fields.requests;

import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateFieldsRequest(
        @NotNull(message = "{features.fields.requests.update.fields.invalid}")
        @Valid
        List<FieldInfo> fields) {
}
