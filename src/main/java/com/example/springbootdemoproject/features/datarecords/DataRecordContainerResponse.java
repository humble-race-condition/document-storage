package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;

import java.util.List;

public record DataRecordContainerResponse(List<DataRecordDetail> dataRecords) {
}
