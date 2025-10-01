package com.example.documentstorage.features.datarecords.requests;


import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UpdateDataRecordRequest(
        @NotEmpty(message = "{features.datarecords.requests.update.datarecord.title.empty}")
        @Length(message = "{features.datarecords.requests.update.datarecord.title.invalid.length}", min = 3, max = 100)
        String title,
        @NotEmpty(message = "{features.datarecords.requests.update.datarecord.description.empty}")
        @Length(message = "{features.datarecords.requests.update.datarecord.description.invalid.length}", min = 3, max = 100)
        String description) {
}
