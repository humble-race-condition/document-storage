package com.example.springbootdemoproject.features.datarecords.requests;

import com.example.springbootdemoproject.features.datarecords.FieldInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateDataRecordRequest(
        @NotEmpty
        @Length(min = 3, max = 100)
        String title,
        @NotEmpty
        @Length(min = 3, max = 100)
        String description,
        @NotNull
        @Valid
        List<FieldInfo> fields) {}
