package com.example.documentstorage.unit.features.fields;

import com.example.documentstorage.features.fields.FieldDataRecordRepository;
import com.example.documentstorage.features.fields.FieldServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class FieldServiceTests {

    @Mock
    private FieldDataRecordRepository repository;

    @InjectMocks
    private FieldServiceImpl fieldService;

    @Test
    void updateFields_whenSubmittingEmptyFieldName_shouldReturnError() {

    }
}
