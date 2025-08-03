package com.example.springbootdemoproject.features.documents;

import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    /**
     * Creats a data record
     */
    @Override
    public void createDocument(CreateDataRecordRequest request) {
        System.out.println("create document");
    }
}
