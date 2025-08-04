package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Field;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    private final DataRecordRepository dataRecordRepository;

    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository) {
        this.dataRecordRepository = dataRecordRepository;
    }

    /**
     * Creats a data record
     */
    @Override
    public void createDocument(CreateDataRecordRequest request) {
        List<Field> fields = request.fields().stream()
                .map(f -> {
                    Field fieldEntity = new Field();
                    fieldEntity.setName(f.name());
                    fieldEntity.setValue(f.value());
                    return fieldEntity;
                })
                .toList();
        DataRecord record = new DataRecord();
        fields.forEach(record::addField);

        dataRecordRepository.saveAndFlush(record);
    }
}
