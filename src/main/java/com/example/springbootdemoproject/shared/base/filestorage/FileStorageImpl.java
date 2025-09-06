package com.example.springbootdemoproject.shared.base.filestorage;

import com.example.springbootdemoproject.features.sections.SectionServiceImpl;
import com.example.springbootdemoproject.shared.base.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
import com.example.springbootdemoproject.shared.base.exceptions.InvalidSystemStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageImpl implements FileStorage {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageImpl.class);

    private final LocalizationService localizationService;
    private final String basePath;

    public FileStorageImpl(
            LocalizationService localizationService,
            @Value("${document.storage.path}") String basePath) {
        this.localizationService = localizationService;
        this.basePath = basePath;
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
}
