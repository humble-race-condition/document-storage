package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;

public interface DataRecordService {

    /**
     * Returns a data record details by its id
     *
     * @param id of the data record
     * @return the data record details with their sections and fields
     */
    DataRecordDetail getDataRecordById(int id);

    /**
     * Retrieves all data records filtered by the specified parameters
     *
     * @param title       of the data record
     * @param description of the data record
     * @return the list of data record details
     */
    DataRecordContainerResponse getDataRecords(String title, String description);

    DataRecordDetail createDataRecord(CreateDataRecordRequest request);

    DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request);
}
