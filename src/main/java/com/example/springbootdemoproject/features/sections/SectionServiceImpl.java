package com.example.springbootdemoproject.features.sections;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Section;
import com.example.springbootdemoproject.shared.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import com.example.springbootdemoproject.shared.base.models.responses.SectionDetail;
import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
import com.example.springbootdemoproject.shared.base.exceptions.InvalidClientInputException;
import com.example.springbootdemoproject.shared.base.exceptions.InvalidSystemStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {
    private static final Logger logger = LoggerFactory.getLogger(SectionServiceImpl.class);

    private final SectionRepository sectionRepository;
    private final LocalizationService localizationService;
    private final String basePath;

    public SectionServiceImpl(
            SectionRepository sectionRepository,
            LocalizationService localizationService,
            @Value("${document.storage.path}") String basePath) {
        this.sectionRepository = sectionRepository;
        this.localizationService = localizationService;
        this.basePath = basePath;
    }

    /**
     * Uploads a section to the specified data record
     * @param dataRecordId the data record to which the section is attached
     * @param sectionFile  the binary file that will be uploaded
     * @return Returns the id of the newly uploaded section
     */
    @Override
    public DataRecordDetail uploadSection(int dataRecordId, MultipartFile sectionFile) {
        //ToDo transaction management
        DataRecord dataRecord = sectionRepository
                .findById(dataRecordId)
                .orElseThrow(() -> {
                    logger.error("Data record with id '{}' not found for section upload", dataRecordId);
                    ErrorMessage errorMessage = localizationService.getErrorMessage("features.sections.on.section.upload.datarecord.not.found", dataRecordId);
                    return new InvalidClientInputException(errorMessage);
                });

        Path storagePath = Paths.get(basePath);
        createDirectoryIfNotPresent(storagePath);

        String fileName = sectionFile.getOriginalFilename();
        Path filePath = Paths.get(basePath, fileName);

        Section sectionRecord = new Section();
        sectionRecord.setFileName(sectionFile.getOriginalFilename());
        sectionRecord.setStorageLocation(filePath.toString());
        dataRecord.addSection(sectionRecord);

        storeSection(sectionFile, filePath);

        dataRecord = sectionRepository.saveAndFlush(dataRecord);

        List<SectionDetail> sectionDetails = dataRecord.getSections().stream()
                .map(f -> new SectionDetail(f.getId(), f.getFileName(), f.getStorageLocation()))
                .toList();

        DataRecordDetail recordDetail = DataRecordDetail
                .withSections(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), sectionDetails);

        logger.info("Uploaded section '{}' to data record '{}'", sectionRecord.getId(), dataRecordId);
        return recordDetail;
    }

    /**
     * Deletes a section from
     * @param dataRecordId the id of the data record
     * @param sectionId    the id of the section
     * @return the updated section details
     */
    @Override
    public DataRecordDetail deleteSection(int dataRecordId, int sectionId) {
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
        removeStoredSection(removedSection);
        dataRecord.getSections().remove(removedSection);

        List<SectionDetail> sectionDetails = dataRecord.getSections().stream()
                .map(f -> new SectionDetail(f.getId(), f.getFileName(), f.getStorageLocation()))
                .toList();

        dataRecord = sectionRepository.saveAndFlush(dataRecord);

        DataRecordDetail recordDetail = DataRecordDetail
                .withSections(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), sectionDetails);

        logger.info("Removed section '{}' to data record '{}'", sectionId, dataRecordId);
        return recordDetail;
    }

    private void createDirectoryIfNotPresent(Path storagePath) {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            logger.error("Unable to create a base directory '{}'", basePath);
            ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");
            throw new InvalidSystemStateException(errorMessage, e);
        }
    }

    private void storeSection(MultipartFile sectionFile, Path filePath) {
        try {
            sectionFile.transferTo(filePath);
        } catch (Exception e) {
            logger.error("Unable to store file '{}'", filePath);
            ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");
            throw new InvalidSystemStateException(errorMessage, e);
        }
    }

    private void removeStoredSection(Section removedSection) {
        String storageLocation = removedSection.getStorageLocation();
        try {
            Files.delete(Paths.get(storageLocation));
        } catch (IOException e) {
            logger.error("Unable to remove stored section '{}'", storageLocation);
            ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");
            throw new InvalidSystemStateException(errorMessage, e);
        }
    }
}
