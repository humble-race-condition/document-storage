package com.example.springbootdemoproject.features.datarecords;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDataRecordRequest(
        @NotNull
        @Valid
        List<FieldInfo> fields) {}
