package com.example.springbootdemoproject.features.datarecords;

import org.springframework.stereotype.Service;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    /**
     * Creats a data record
     */
    @Override
    public void createDocument(CreateDataRecordRequest request) {
        System.out.println("create document");
    }
}
