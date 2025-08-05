package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.responses.DataRecordDetail;

public interface DataRecordService {
    /**
     * Creates a data record with the specified fields
     *
     * @param request The request for the data record
     * @return the data record details
     */
    DataRecordDetail createDataRecord(CreateDataRecordRequest request);

    /**
     * Updated the data record
     * @param id of the data record
     * @param request {@link UpdateDataRecordRequest} The modified values
     * @return {@link UpdateDataRecordRequest} the updated DataRecordDetails
     */
    DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request);
}
