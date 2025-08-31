package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;

public interface DataRecordService {

    /**
     * Returns a data record details by its id
     * @param id of the data record
     * @return the data record details with their sections and fields
     */
    DataRecordDetail getDataRecordById(int id);

    DataRecordDetail createDataRecord(CreateDataRecordRequest request);

    DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request);
}
