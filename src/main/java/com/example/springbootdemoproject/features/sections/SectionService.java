package com.example.springbootdemoproject.features.sections;

import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import org.springframework.web.multipart.MultipartFile;

public interface SectionService {
    DataRecordDetail uploadSection(int dataRecordId, MultipartFile sectionFile);

    DataRecordDetail deleteSection(int dataRecordId, int sectionId);
}
