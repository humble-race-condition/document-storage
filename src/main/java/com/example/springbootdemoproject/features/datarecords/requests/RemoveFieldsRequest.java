package com.example.springbootdemoproject.features.datarecords.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RemoveFieldsRequest(
        @NotNull
        @Valid
        List<String> fieldNames) {
}
