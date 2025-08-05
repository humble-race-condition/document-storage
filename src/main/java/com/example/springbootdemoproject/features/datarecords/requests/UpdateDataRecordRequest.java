package com.example.springbootdemoproject.features.datarecords.requests;


import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UpdateDataRecordRequest(
        @NotEmpty
        @Length(min = 3, max = 100)
        String title,
        @NotEmpty
        @Length(min = 3, max = 100)
        String description) {
}
