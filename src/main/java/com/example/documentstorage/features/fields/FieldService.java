package com.example.documentstorage.features.fields;

import com.example.documentstorage.features.fields.requests.RemoveFieldsRequest;
import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;

public interface FieldService {
    DataRecordDetail updateDataRecordFields(int id, UpdateFieldsRequest request);

    DataRecordDetail removeDataRecordFields(int id, RemoveFieldsRequest request);
}
