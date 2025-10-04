package com.example.documentstorage.features.fields;

import com.example.documentstorage.entities.DataRecord;
import com.example.documentstorage.entities.Field;
import com.example.documentstorage.features.fields.requests.RemoveFieldsRequest;
import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
import com.example.documentstorage.shared.base.models.requests.FieldInfo;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import com.example.documentstorage.shared.base.models.responses.FieldDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {
    private static final Logger logger = LoggerFactory.getLogger(FieldServiceImpl.class);
    private final FieldDataRecordRepository dataRecordRepository;

    public FieldServiceImpl(FieldDataRecordRepository dataRecordRepository) {
        this.dataRecordRepository = dataRecordRepository;
    }

    @Override
    public DataRecordDetail updateDataRecordFields(int id, UpdateFieldsRequest request) {
        validateRequest(request);

        DataRecord dataRecord = dataRecordRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Data record with id '{}' not found for data record field update", id);
                    return new InvalidClientInputException("features.fields.on.update.datarecord.datarecord.not.found", id);
                });

        List<Field> recordFields = dataRecord.getFields();
        Map<String, Field> existingFieldByName = recordFields.stream()
                .collect(Collectors.toMap(Field::getName, x -> x));

        List<FieldInfo> requestedFields = filterDuplicateFieldNames(request);

        for (FieldInfo requestField : requestedFields) {
            if (existingFieldByName.containsKey(requestField.name())) {
                updateExistingField(existingFieldByName, requestField);
            } else {
                addFieldToDataRecord(dataRecord, requestField);
            }
        }

        dataRecordRepository.saveAndFlush(dataRecord);

        List<FieldDetail> fieldDetails = dataRecord.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();
        logger.info("Updated fields for data record with id '{}'", id);
        return DataRecordDetail.withFields(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), fieldDetails);
    }

    @Override
    public DataRecordDetail removeDataRecordFields(int id, RemoveFieldsRequest request) {
        validateRequest(request);

        DataRecord dataRecord = dataRecordRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Data record with id '{}' not found for data record field remove", id);
                    return new InvalidClientInputException("features.fields.on.remove.datarecord.datarecord.not.found", id);
                });

        //ToDo add logic for unique field name constraints. unique index by datarecord id and field name. This way, only
        //one field of the same name can be added to a specific data record
        for (String fieldName : request.fields()) {
            dataRecord.getFields().stream()
                    .filter(f -> f.getName().equals(fieldName))
                    .findFirst()
                    .ifPresent(dataRecord::removeField);
        }

        dataRecordRepository.saveAndFlush(dataRecord);

        List<FieldDetail> fieldDetails = dataRecord.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();
        logger.info("Removed fields for data record with id '{}'", id);
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

    private static List<FieldInfo> filterDuplicateFieldNames(UpdateFieldsRequest request) {
        List<FieldInfo> requestedFields = request.fields().stream()
                .collect(Collectors.toMap(
                        FieldInfo::name,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(FieldInfo::value)))
                )
                .values()
                .stream()
                .toList();
        return requestedFields;
    }

    private void validateRequest(RemoveFieldsRequest request) {
        Objects.requireNonNull(request);
    }

    private void validateRequest(UpdateFieldsRequest request) {
        Objects.requireNonNull(request);
    }
}
