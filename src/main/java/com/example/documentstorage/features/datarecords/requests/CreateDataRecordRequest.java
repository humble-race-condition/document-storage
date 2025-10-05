package com.example.documentstorage.features.datarecords.requests;

import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateDataRecordRequest(
        @NotEmpty(message = "{features.datarecords.requests.create.datarecord.title.empty}")
        @Length(message = "{features.datarecords.requests.create.datarecord.title.invalid.length}", min = 3, max = 100)
        String title,
        @NotEmpty(message = "{features.datarecords.requests.create.datarecord.description.empty}")
        @Length(message = "{features.datarecords.requests.create.datarecord.description.invalid.length}", min = 3, max = 100)
        String description,
        @NotNull(message = "{features.datarecords.requests.create.datarecord.fields.invalid}")
        @Valid
        List<FieldInfo> fields) {
}
