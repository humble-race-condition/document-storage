package com.example.springbootdemoproject.features.sections;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
            String baseFolder = System.getProperty("user.dir") + "/files/";
            File file = new File(baseFolder + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            section.transferTo(file);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "ok";
    }
}
