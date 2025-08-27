package com.example.springbootdemoproject.features.sections;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SectionServiceImpl implements SectionService {
    /**
     * @param section
     * @return
     */
    @Override
    public String uploadSection(MultipartFile section) {
        String fileName = section.getOriginalFilename();
        long size = section.getSize();

        // Example: save it to disk
        try {
            section.transferTo(new java.io.File("tmp/" + fileName));
        } catch (Exception e) {
            return e.getMessage();
        }

        return "ok";
    }
}
