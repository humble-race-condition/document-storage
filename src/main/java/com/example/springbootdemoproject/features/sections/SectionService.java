package com.example.springbootdemoproject.features.sections;

import org.springframework.web.multipart.MultipartFile;

public interface SectionService {
    String uploadSection(MultipartFile section);
}
