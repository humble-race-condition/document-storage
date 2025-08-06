package com.example.springbootdemoproject.features.datarecords.requests;

import com.example.springbootdemoproject.features.datarecords.FieldInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateFieldsRequest(
        @NotNull
        @Valid
        List<FieldInfo> fields) {
}
