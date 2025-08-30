package com.example.springbootdemoproject.features.sections;

import com.example.springbootdemoproject.entities.DataRecord;
import com.example.springbootdemoproject.entities.Section;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import com.example.springbootdemoproject.shared.base.models.responses.SectionDetail;
import com.example.springbootdemoproject.shared.exceptions.InvalidClientInputException;
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

    private final SectionRepository sectionRepository;
    private final String basePath;

    public SectionServiceImpl(
            SectionRepository sectionRepository,
            @Value("${document.storage.path}") String basePath) {
        this.sectionRepository = sectionRepository;
        this.basePath = basePath;
    }

    /**
     * Uploads a section to the specified
     *
     * @param dataRecordId the data record to whitch the section is attached
     * @param sectionFile  the binary file that will be uploaded
     * @return Returns the id of the newly uploaded section
     */
    @Override
    public DataRecordDetail uploadSection(int dataRecordId, MultipartFile sectionFile) {
        DataRecord dataRecord = sectionRepository
                .findById(dataRecordId)
                .orElseThrow(InvalidClientInputException::new);

        String fileName = sectionFile.getOriginalFilename();
        Path storagePath = Paths.get(basePath);
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path filePath = Paths.get(basePath, fileName);

        Section sectionRecord = new Section();
        sectionRecord.setFileName(sectionFile.getOriginalFilename());
        sectionRecord.setStorageLocation(filePath.toString());
        dataRecord.addSection(sectionRecord);

        try {
            sectionFile.transferTo(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dataRecord = sectionRepository.saveAndFlush(dataRecord);

        List<SectionDetail> sectionDetails = dataRecord.getSections().stream()
                .map(f -> new SectionDetail(f.getId(), f.getFileName(), f.getStorageLocation()))
                .toList();

        DataRecordDetail recordDetail = DataRecordDetail
                .withSections(dataRecord.getId(), dataRecord.getTitle(), dataRecord.getDescription(), sectionDetails);
        return recordDetail;
    }
}
