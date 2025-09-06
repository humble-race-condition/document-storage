package com.example.springbootdemoproject.features.sections;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Section;
import com.example.springbootdemoproject.entities.TransactionActionRecord;
import com.example.springbootdemoproject.shared.base.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
import com.example.springbootdemoproject.shared.base.exceptions.InvalidClientInputException;
import com.example.springbootdemoproject.shared.base.filestorage.FileStorage;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import com.example.springbootdemoproject.shared.base.models.responses.SectionDetail;
import com.example.springbootdemoproject.shared.base.filestorage.ActionType;
import com.example.springbootdemoproject.shared.base.filestorage.TransactionActionRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class SectionServiceImpl implements SectionService {
    private static final Logger logger = LoggerFactory.getLogger(SectionServiceImpl.class);

    private final SectionRepository sectionRepository;
    private final TransactionActionRecordRepository transactionActionRepository;
    private final TransactionTemplate transactionTemplate;
    private final FileStorage fileStorage;
    private final LocalizationService localizationService;

    public SectionServiceImpl(
            SectionRepository sectionRepository,
            TransactionActionRecordRepository transactionActionRecordRepository,
            TransactionTemplate transactionTemplate,
            FileStorage fileStorage,
            LocalizationService localizationService) {
        this.sectionRepository = sectionRepository;
        this.transactionActionRepository = transactionActionRecordRepository;
        this.transactionTemplate = transactionTemplate;
        this.fileStorage = fileStorage;
        this.localizationService = localizationService;
    }

    /**
     * Uploads a section to the specified data record
     *
     * @param dataRecordId the data record to which the section is attached
     * @param sectionFile  the binary file that will be uploaded
     * @return Returns the id of the newly uploaded section
     */
    @Override
    public DataRecordDetail uploadSection(int dataRecordId, MultipartFile sectionFile) {
        //ToDo fix nulls
        //ToDo fix path. Store only the relative path in the database. Check the variables in application.properties
        //ToDo fix return type of the method
        //But, the good part is, that the core of the idea, works!
        String fileName = sectionFile.getOriginalFilename();
        String systemFileName = fileStorage.generateSystemFileName(fileName);

        TransactionActionRecord actionRecord = transactionTemplate
                .execute(status -> addCreateTransactionAction(status, systemFileName));

        Section section = transactionTemplate
                .execute(status -> storeSection(actionRecord, dataRecordId, sectionFile, systemFileName));

        List<SectionDetail> sectionDetails = section.getDataRecord().getSections().stream()
                .map(f -> new SectionDetail(f.getId(), f.getFileName(), f.getStorageLocation()))
                .toList();

        DataRecordDetail recordDetail = DataRecordDetail
                .withSections(section.getDataRecord().getId(), section.getDataRecord().getTitle(), section.getDataRecord().getDescription(), sectionDetails);

        logger.info("Uploaded section '{}' to data record '{}'", section.getId(), dataRecordId);
        return recordDetail;
    }

    //ToDo update postman collections in resources.
    //ToDo fix get all ordering of sorts with an additional field

    /**
     * Deletes a section from
     *
     * @param dataRecordId the id of the data record
     * @param sectionId    the id of the section
     */
    @Override
    public void deleteSection(int dataRecordId, int sectionId) {
        DataRecord dataRecord = sectionRepository
                .findById(dataRecordId)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for section removal", dataRecordId);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.sections.on.section.removal.datarecord.not.found", dataRecordId);
                    return new InvalidClientInputException(errorMessage);
                });

        List<Section> sections = dataRecord.getSections()
                .stream()
                .filter(s -> s.getId() == sectionId)
                .toList();

        if (sections.size() != 1) {
            logger.error("Data record with id '{}' either has zero or more than one sections with the section id '{}'", dataRecordId, sectionId);
            ErrorMessage errorMessage = localizationService.getErrorMessage("features.sections.on.section.removal.multiple.sections", sectionId);
            throw new InvalidClientInputException(errorMessage);
        }

        Section removedSection = sections.getFirst();
        TransactionActionRecord actionRecord = addDeleteTransactionAction(removedSection.getStorageLocation());
        deleteSection(dataRecord, removedSection, actionRecord);

        logger.info("Removed section '{}' to data record '{}'", sectionId, dataRecordId);
    }

    private void deleteSection(
            DataRecord dataRecord,
            Section removedSection,
            TransactionActionRecord actionRecord) {
        dataRecord.getSections().remove(removedSection);
        actionRecord.setCommitted(true);
    }

    private TransactionActionRecord addCreateTransactionAction(TransactionStatus status, String systemFileName) {
        TransactionActionRecord actionRecord = new TransactionActionRecord();
        actionRecord.setStorageLocation(systemFileName);
        actionRecord.setActionType(ActionType.CREATE);

        transactionActionRepository.save(actionRecord);

        return actionRecord;
    }

    private TransactionActionRecord addDeleteTransactionAction(String systemFileName) {
        TransactionActionRecord actionRecord = new TransactionActionRecord();
        actionRecord.setStorageLocation(systemFileName);
        actionRecord.setActionType(ActionType.DELETE);

        transactionActionRepository.save(actionRecord);

        return actionRecord;
    }

    private Section storeSection(TransactionActionRecord actionRecord,
                                 int dataRecordId,
                                 MultipartFile sectionFile,
                                 String systemFileName) {
        DataRecord dataRecord = sectionRepository
                .findById(dataRecordId)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for section upload", dataRecordId);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.sections.on.section.upload.datarecord.not.found", dataRecordId);
                    return new InvalidClientInputException(errorMessage);
                });

        Section sectionRecord = new Section();
        sectionRecord.setFileName(sectionFile.getOriginalFilename());
        sectionRecord.setStorageLocation(systemFileName);
        dataRecord.addSection(sectionRecord);

        fileStorage.storeSection(sectionFile, systemFileName);

        actionRecord.setCommitted(true);

        return sectionRecord;
    }
}
