package com.example.documentstorage.features.sections;

import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import org.springframework.web.multipart.MultipartFile;

public interface SectionService {
    /**
     * Uploads a section to the specified data record
     *
     * @param dataRecordId the data record to which the section is attached
     * @param sectionFile  the binary file that will be uploaded
     * @return Returns the id of the newly uploaded section
     */
    DataRecordDetail uploadSection(int dataRecordId, MultipartFile sectionFile);

    /**
     * Deletes a section from
     *
     * @param dataRecordId the id of the data record
     * @param sectionId    the id of the section
     */
    void deleteSection(int dataRecordId, int sectionId);
}
