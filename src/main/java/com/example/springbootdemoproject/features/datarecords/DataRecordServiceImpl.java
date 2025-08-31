package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Field;
import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.shared.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import com.example.springbootdemoproject.shared.base.models.responses.FieldDetail;
import com.example.springbootdemoproject.shared.exceptions.ErrorMessage;
import com.example.springbootdemoproject.shared.exceptions.InvalidClientInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataRecordServiceImpl implements DataRecordService {
    private static final Logger logger = LoggerFactory.getLogger(DataRecordServiceImpl.class);

    private final DataRecordRepository dataRecordRepository;
    private final LocalizationService localizationService;

    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository, LocalizationService localizationService) {
        this.dataRecordRepository = dataRecordRepository;
        this.localizationService = localizationService;
    }

    //ToDo Soft Delete on datarecords
    //ToDo unique for fields, and add a check
    //ToDo add flyway sometime, but for now, its easier this way
    //ToDo add file cabinets

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

        logger.info("Data record with id '{}' created", dataRecordDetail.id());
        return dataRecordDetail;
    }

    @Override
    public DataRecordDetail updateDataRecord(int id, UpdateDataRecordRequest request) {
        validateRequest(request);

        DataRecord dataRecord = dataRecordRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for data record update", id);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.datarecords.on.datarecord.update.datarecord.not.found", id);
                    return new InvalidClientInputException(errorMessage);
                });

        dataRecord.setTitle(request.title());
        dataRecord.setDescription(request.description());

        dataRecordRepository.saveAndFlush(dataRecord);

        DataRecordDetail dataRecordDetail = DataRecordDetail.fromBase(id, request.title(), request.description());

        logger.info("Data record with id '{}' updated", dataRecordDetail.id());
        return dataRecordDetail;
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
