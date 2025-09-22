package com.example.documentstorage.features.datarecords;

import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;

import java.util.List;

public record DataRecordContainerResponse(List<DataRecordDetail> dataRecords) {
}
