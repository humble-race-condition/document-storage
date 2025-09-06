package com.example.springbootdemoproject.shared.base.filestorage;

import com.example.springbootdemoproject.shared.base.apimessages.LocalizationService;
import com.example.springbootdemoproject.shared.base.exceptions.ErrorMessage;
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
import java.time.LocalDateTime;
import java.util.Objects;

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

    /**
     * Generates the system file name based on the original file name
     *
     * @param originalFileName the original file name
     * @return the system file name
     */
    @Override
    public String generateSystemFileName(String originalFileName) {
        //ToDo handle NullPointerException everywhere. Actually change this exception to one of mine custom exceptions.
        Objects.requireNonNull(originalFileName, () -> localizationService.getMessage("shared.base.filestorage.on.generate.file.name.file.name.null"));
        int lastIndexOf = originalFileName.lastIndexOf(".");
        String fileName = originalFileName.substring(0, lastIndexOf);
        String extension = originalFileName.substring(lastIndexOf);
        String systemFileName = String.format("%s%s%s", fileName, LocalDateTime.now(), extension);
        return systemFileName;
    }

    /**
     * Stores the section file using the system file name
     *
     * @param sectionFile    the file to be stored
     * @param systemFileName the system file name
     */
    @Override
    public void storeSection(MultipartFile sectionFile, String systemFileName) {
        createBaseDirectoryIfNotPresent();

        Path filePath = getFullPath(systemFileName);
        try {
            sectionFile.transferTo(filePath);
        } catch (Exception e) {
            logger.error("Unable to store file '{}'", filePath);
            ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");
            throw new InvalidSystemStateException(errorMessage, e);
        }
    }

    /**
     * Removes the section file using the system file name
     *
     * @param systemFileName the system file name
     */
    @Override
    public void removeSection(String systemFileName) {
        Path filePath = getFullPath(systemFileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            logger.error("Unable to remove stored section '{}'", filePath);
            ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");
            throw new InvalidSystemStateException(errorMessage, e);
        }
    }

    private Path getFullPath(String systemFileName) {
        return Paths.get(basePath, systemFileName);
    }

    private void createBaseDirectoryIfNotPresent() {
        try {
            Path storagePath = Path.of(basePath);
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            logger.error("Unable to create a base directory '{}'", basePath);
            ErrorMessage errorMessage = localizationService.getErrorMessage("default.error.message");
            throw new InvalidSystemStateException(errorMessage, e);
        }
    }
}
