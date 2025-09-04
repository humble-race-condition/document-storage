package com.example.springbootdemoproject.features.fields;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Field;
import com.example.springbootdemoproject.shared.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.models.requests.FieldInfo;
import com.example.springbootdemoproject.features.fields.requests.RemoveFieldsRequest;
import com.example.springbootdemoproject.features.fields.requests.UpdateFieldsRequest;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import com.example.springbootdemoproject.shared.base.models.responses.FieldDetail;
import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
import com.example.springbootdemoproject.shared.base.exceptions.InvalidClientInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {
    private static final Logger logger = LoggerFactory.getLogger(FieldServiceImpl.class);
    private final FieldRepository fieldRepository;
    private final LocalizationService localizationService;

    public FieldServiceImpl(FieldRepository fieldRepository, LocalizationService localizationService) {
        this.fieldRepository = fieldRepository;
        this.localizationService = localizationService;
    }

    @Override
    public DataRecordDetail updateDataRecordFields(int id, UpdateFieldsRequest request) {
        validateRequest(request);

        DataRecord dataRecord = fieldRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for data record field update", id);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.fields.on.update.datarecord.datarecord.not.found", id);
                    return new InvalidClientInputException(errorMessage);
                });

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

        dataRecord = fieldRepository.saveAndFlush(dataRecord);
        logger.info("Updated fields for data record with id '{}'", id);
        return DataRecordDetail.withFields(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), fieldDetails);
    }

    @Override
    public DataRecordDetail removeDataRecordFields(int id, RemoveFieldsRequest request) {
        validateRequest(request);

        DataRecord dataRecord = fieldRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for data record field remove", id);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.fields.on.remove.datarecord.datarecord.not.found", id);
                    return new InvalidClientInputException(errorMessage);
                });

        //ToDo add logic for unique field name constraints. unique index by datarecord id and field name. This way, only
        //one field of the same name can be added to a specific data record
        for (String fieldName : request.fields()) {
            dataRecord.getFields().stream()
                    .filter(f -> f.getName().equals(fieldName))
                    .findFirst()
                    .ifPresent(dataRecord::removeField);
        }

        dataRecord = fieldRepository.saveAndFlush(dataRecord);

        List<FieldDetail> fieldDetails = dataRecord.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();

        //ToDo do not return DataRecordDetail, Return only field details. This prevents a database fetch. Do this in section service as well
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

    private void validateRequest(RemoveFieldsRequest request) {
        Objects.requireNonNull(request);
    }

    private void validateRequest(UpdateFieldsRequest request) {
        Objects.requireNonNull(request);
    }
}
