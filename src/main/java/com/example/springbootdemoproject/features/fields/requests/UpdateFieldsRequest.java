package com.example.springbootdemoproject.features.fields.requests;

import com.example.springbootdemoproject.shared.base.models.requests.FieldInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateFieldsRequest(
        @NotNull
        @Valid
        List<FieldInfo> fields) {
}
