package com.example.springbootdemoproject.features.fields;

import com.example.springbootdemoproject.features.fields.requests.RemoveFieldsRequest;
import com.example.springbootdemoproject.features.fields.requests.UpdateFieldsRequest;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;

public interface FieldService {
    DataRecordDetail updateDataRecordFields(int id, UpdateFieldsRequest request);

    DataRecordDetail removeDataRecordFields(int id, RemoveFieldsRequest request);
}
