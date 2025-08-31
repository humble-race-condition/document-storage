package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Field;
import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.shared.base.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
import com.example.springbootdemoproject.shared.base.exceptions.InvalidClientInputException;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import com.example.springbootdemoproject.shared.base.models.responses.FieldDetail;
import com.example.springbootdemoproject.shared.base.models.responses.SectionDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DataRecordServiceImpl implements DataRecordService {
    private static final Logger logger = LoggerFactory.getLogger(DataRecordServiceImpl.class);

    private final DataRecordRepository dataRecordRepository;
    private final LocalizationService localizationService;

    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository, LocalizationService localizationService) {
        this.dataRecordRepository = dataRecordRepository;
        this.localizationService = localizationService;
    }

    /**
     * Returns a data record details by its id
     *
     * @param id of the data record
     * @return the data record details with their sections and fields
     */
    @Override
    public DataRecordDetail getDataRecordById(int id) {
        DataRecord record = dataRecordRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for data record get by id", id);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.datarecords.on.datarecord.get.by.id.datarecord.not.found", id);
                    return new InvalidClientInputException(errorMessage);
                });

        List<FieldDetail> fieldDetails = record.getFields().stream()
                .map(f -> new FieldDetail(f.getId(), f.getName(), f.getValue()))
                .toList();

        List<SectionDetail> sectionDetails = record.getSections().stream()
                .map(s -> new SectionDetail(s.getId(), s.getFileName(), s.getStorageLocation()))
                .toList();

        DataRecordDetail dataRecordDetail = new DataRecordDetail(record.getId(), record.getTitle(), record.getDescription(), fieldDetails, sectionDetails);

        logger.info("Retrieved data record by id '{}'", id);
        return dataRecordDetail;
    }

    /**
     * Retrieves all data records filtered by the specified parameters
     *
     * @param title       of the data record
     * @param description of the data record
     * @param page        of the request, starting from 0
     * @param size        of the page
     * @param sort        of the result
     * @return the list of data record details
     */
    @Override
    public DataRecordContainerResponse getDataRecords(String title, String description, int page, int size, String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String sortParam : sort) {
            String[] parts = sortParam.split(",");
            if (parts.length == 2) {
                orders.add(new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]));
            } else {
                orders.add(new Sort.Order(Sort.Direction.ASC, parts[0]));
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Specification<DataRecord> spec = DataRecordSpecification.hasTitle(title).or(DataRecordSpecification.hasDescription(description));
        Page<DataRecord> dataRecords = dataRecordRepository.findAll(spec, pageable);

        List<DataRecordDetail> dataRecordDetails = dataRecords.stream().map(record -> {
            DataRecordDetail dataRecordDetail = DataRecordDetail.fromBase(record.getId(), record.getTitle(), record.getDescription());
            return dataRecordDetail;
        }).toList();

        logger.info("Retrieved data record details");
        return new DataRecordContainerResponse(dataRecordDetails);
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
