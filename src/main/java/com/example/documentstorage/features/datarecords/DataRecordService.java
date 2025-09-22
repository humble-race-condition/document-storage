package com.example.documentstorage.features.datarecords;

import com.example.documentstorage.features.datarecords.requests.CreateDataRecordRequest;
import com.example.documentstorage.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.documentstorage.shared.base.registers.FilterCriteria;
import com.example.documentstorage.shared.base.registers.PaginationCriteria;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;

public interface DataRecordService {

    /**
     * Returns a data record details by its id
     *
     * @param id of the data record
     * @return the data record details with their sections and fields
     */
    DataRecordDetail getDataRecordById(int id);

    DataRecordContainerResponse getDataRecords(FilterCriteria filterCriteria, PaginationCriteria paginationCriteria);

    DataRecordDetail createDataRecord(CreateDataRecordRequest request);

    DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request);
}
