package com.example.springbootdemoproject.features.datarecords;

public interface DataRecordService {
    /**
     * Creats a data record
     * @param request
     */
    void createDocument(CreateDataRecordRequest request);
}
