package com.example.documentstorage.features.datarecords;

import com.example.documentstorage.entities.DataRecord;
import com.example.documentstorage.entities.Field;
import com.example.documentstorage.features.datarecords.requests.CreateDataRecordRequest;
import com.example.documentstorage.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
import com.example.documentstorage.shared.base.registers.FilterCriteria;
import com.example.documentstorage.shared.base.registers.PaginationCriteria;
import com.example.documentstorage.shared.base.registers.RegisterCriteriaParser;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import com.example.documentstorage.shared.base.models.responses.FieldDetail;
import com.example.documentstorage.shared.base.models.responses.SectionDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DataRecordServiceImpl implements DataRecordService {
    private static final Logger logger = LoggerFactory.getLogger(DataRecordServiceImpl.class);

    private final DataRecordRepository dataRecordRepository;
    private final RegisterCriteriaParser registerCriteriaParser;
    private final LocalizationService localizationService;

    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository,
                                 RegisterCriteriaParser registerCriteriaParser,
                                 LocalizationService localizationService) {
        this.dataRecordRepository = dataRecordRepository;
        this.registerCriteriaParser = registerCriteriaParser;
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
     * @param filterCriteria contains the criteria by which the data is filtered
     * @param paginationCriteria contains the pagination requirements
     * @return the paginated data records
     */
    @Override
    public DataRecordContainerResponse getDataRecords(FilterCriteria filterCriteria, PaginationCriteria paginationCriteria) {
        Pageable pageable = registerCriteriaParser.parsePaginationCriteria(paginationCriteria);
        Specification<DataRecord> spec = registerCriteriaParser.parseFilterCriteria(filterCriteria);

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
