package com.example.springbootdemoproject.features.sections;

import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import org.springframework.web.multipart.MultipartFile;

public interface SectionService {
    /**
     * Uploads a section to the specified data record
     * @param dataRecordId the data record to which the section is attached
     * @param sectionFile  the binary file that will be uploaded
     * @return Returns the id of the newly uploaded section
     */
    DataRecordDetail uploadSection(int dataRecordId, MultipartFile sectionFile);

    /**
     * Deletes a section from
     * @param dataRecordId the id of the data record
     * @param sectionId    the id of the section
     * @return the updated section details
     */
    DataRecordDetail deleteSection(int dataRecordId, int sectionId);
}
