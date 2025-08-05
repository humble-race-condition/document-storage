package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Field;
import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.responses.DataRecordDetail;
import com.example.springbootdemoproject.features.datarecords.responses.FieldDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    private final DataRecordRepository dataRecordRepository;

    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository) {
        this.dataRecordRepository = dataRecordRepository;
    }

    /**
     * Creates a data record with the specified fields
     *
     * @param request The request for the data record
     * @return the data record details
     */
    @Override
    public DataRecordDetail createDataRecord(CreateDataRecordRequest request) {
        validateRequest(request);

        List<Field> fields = request.fields().stream()
                .map(f -> {
                    Field fieldEntity = new Field();
                    fieldEntity.setName(f.name());
                    fieldEntity.setValue(f.value());
                    return fieldEntity;
                })
                .toList();
        DataRecord record = new DataRecord();
        record.setTitle(record.getTitle());
        record.setDescription(record.getDescription());
        fields.forEach(record::addField);

        record = dataRecordRepository.saveAndFlush(record);

        List<FieldDetail> fieldDetails = record.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();


        DataRecordDetail dataRecordDetail = DataRecordDetail
                .withFields(record.getId(), record.getTitle(), record.getDescription(), fieldDetails);

        return dataRecordDetail;
    }

    /**
     * Updated the data record
     * @param id of the data record
     * @param request {@link UpdateDataRecordRequest} The modified values
     * @return {@link UpdateDataRecordRequest} the updated DataRecordDetails
     */
    @Override
    public DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.title());
        Objects.requireNonNull(request.description());

        Optional<DataRecord> dataRecord = dataRecordRepository.findById(id);
        if (dataRecord.isEmpty()) {
            throw new RuntimeException();
        }

        dataRecord.get().setTitle(request.title());
        dataRecord.get().setDescription(request.description());

        dataRecordRepository.saveAndFlush(dataRecord.get());

        DataRecordDetail dataRecordDetail = DataRecordDetail.fromBase(id, request.title(), request.description());
        return dataRecordDetail;
    }



    private static void validateRequest(CreateDataRecordRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.title());
        Objects.requireNonNull(request.description());
        Objects.requireNonNull(request.fields());
    }
}
