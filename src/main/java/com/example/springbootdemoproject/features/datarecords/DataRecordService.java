package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.RemoveFieldsRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateFieldsRequest;
import com.example.springbootdemoproject.features.datarecords.responses.DataRecordDetail;

public interface DataRecordService {

    DataRecordDetail createDataRecord(CreateDataRecordRequest request);

    DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request);

    DataRecordDetail updateDataRecordFields(int id, UpdateFieldsRequest request);

    DataRecordDetail removeDataRecord(int id, RemoveFieldsRequest request);
}
