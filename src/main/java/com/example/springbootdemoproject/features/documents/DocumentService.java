package com.example.springbootdemoproject.features.documents;

public interface DocumentService {
    /**
     * Creats a data record
     * @param request
     */
    void createDocument(CreateDataRecordRequest request);
}
