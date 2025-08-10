package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Field;
import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.RemoveFieldsRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateFieldsRequest;
import com.example.springbootdemoproject.features.datarecords.responses.DataRecordDetail;
import com.example.springbootdemoproject.features.datarecords.responses.FieldDetail;
import com.example.springbootdemoproject.shared.exceptions.InvalidClientInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataRecordServiceImpl implements DataRecordService {
    private static final Logger logger = LoggerFactory.getLogger(DataRecordServiceImpl.class);

    private final DataRecordRepository dataRecordRepository;

    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository) {
        this.dataRecordRepository = dataRecordRepository;
    }

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
        record.setTitle(request.title());
        record.setDescription(request.description());
        fields.forEach(record::addField);

        record = dataRecordRepository.saveAndFlush(record);

        List<FieldDetail> fieldDetails = record.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();

        DataRecordDetail dataRecordDetail = DataRecordDetail
                .withFields(record.getId(), record.getTitle(), record.getDescription(), fieldDetails);

        logger.info("Data record with id \"{}\" created", dataRecordDetail.id());
        return dataRecordDetail;
    }

    @Override
    public DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request) {
        validateRequest(request);

        Optional<DataRecord> dataRecord = dataRecordRepository.findById(id);
        if (dataRecord.isEmpty()) {
            logger.error("Data record with id \"{}\" not found for data record update", id);
            throw new InvalidClientInputException();
        }

        dataRecord.get().setTitle(request.title());
        dataRecord.get().setDescription(request.description());

        dataRecordRepository.saveAndFlush(dataRecord.get());

        DataRecordDetail dataRecordDetail = DataRecordDetail.fromBase(id, request.title(), request.description());

        logger.info("Data record with id \"{}\" updated", dataRecordDetail.id());
        return dataRecordDetail;
    }

    @Override
    public DataRecordDetail updateDataRecordFields(int id, UpdateFieldsRequest request) {
        validateRequest(request);

        Optional<DataRecord> dataRecordOptional = dataRecordRepository.findById(id);
        if (dataRecordOptional.isEmpty()) {
            logger.error("Data record with id \"{}\" not found for data record field update", id);
            throw new InvalidClientInputException();
        }

        DataRecord dataRecord = dataRecordOptional.get();
        List<Field> recordFields = dataRecord.getFields();
        Map<String, Field> existingFieldByName = recordFields.stream()
                .collect(Collectors.toMap(Field::getName, x -> x));
        for (FieldInfo requestField : request.fields()) {
            if (existingFieldByName.containsKey(requestField.name())) {
                updateExistingField(existingFieldByName, requestField);
            } else {
                addFieldToDataRecord(dataRecord, requestField);
            }
        }

        List<FieldDetail> fieldDetails = dataRecord.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();

        dataRecord = dataRecordRepository.saveAndFlush(dataRecord);
        logger.info("Data record with id \"{}\" updated fields", id);
        return DataRecordDetail.withFields(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), fieldDetails);
    }

    @Override
    public DataRecordDetail removeDataRecord(int id, RemoveFieldsRequest request) {
        validateRequest(request);

        Optional<DataRecord> dataRecordOptional = dataRecordRepository.findById(id);
        if (dataRecordOptional.isEmpty()) {
            logger.error("Data record with id \"{}\" not found for data record field remove", id);
            throw new InvalidClientInputException();
        }

        //ToDo add logic for unique field name constraints. unique index by datarecord id and field name. This way, only
        //one field of the same name can be added to a specific data record
        DataRecord dataRecord = dataRecordOptional.get();
        for (String fieldName : request.fields()) {
            dataRecord.getFields().stream()
                    .filter(f -> f.getName().equals(fieldName))
                    .findFirst()
                    .ifPresent(dataRecord::removeField);
        }

        dataRecord = dataRecordRepository.saveAndFlush(dataRecord);

        List<FieldDetail> fieldDetails = dataRecord.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();

        return DataRecordDetail.withFields(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), fieldDetails);
    }

    private void addFieldToDataRecord(DataRecord dataRecord, FieldInfo requestField) {
        Field field = new Field();
        field.setName(requestField.name());
        field.setValue(requestField.value());
        dataRecord.addField(field);
    }

    private void updateExistingField(Map<String, Field> existingFieldsByName, FieldInfo requestField) {
        Field existingField = existingFieldsByName.get(requestField.name());
        existingField.setName(requestField.name());
        existingField.setValue(requestField.value());
    }

    private void validateRequest(RemoveFieldsRequest request) {
        Objects.requireNonNull(request);
    }

    private void validateRequest(UpdateFieldsRequest request) {
        Objects.requireNonNull(request);
    }

    private static void validateRequest(UpdateDataRecordRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.title());
        Objects.requireNonNull(request.description());
    }

    private static void validateRequest(CreateDataRecordRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.title());
        Objects.requireNonNull(request.description());
        Objects.requireNonNull(request.fields());
    }
}
