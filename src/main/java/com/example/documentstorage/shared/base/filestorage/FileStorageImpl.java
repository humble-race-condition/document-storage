package com.example.documentstorage.shared.base.filestorage;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.exceptions.InvalidSystemStateException;
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
import java.time.format.DateTimeFormatter;
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
     * Downloads the file by the given file name
     *
     * @param systemFileName the system file name
     * @return the system file name
     */
    @Override
    public byte[] downloadFile(String systemFileName) {
        Objects.requireNonNull(systemFileName, localizationService.getMessage("shared.base.filestorage.on.download.file.system.file.name.null"));
        Path filePath = getFullPath(systemFileName);
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            logger.info("Read file '{}' on download section", filePath);

            return bytes;
        } catch (IOException e) {
            logger.error("Unable to read stored section '{}'", filePath);
            throw new InvalidSystemStateException(e);
        }
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssnnnnnnnnn");
        String systemFileName = String.format("%s_%s%s", fileName, LocalDateTime.now().format(formatter), extension);
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
            throw new InvalidSystemStateException(e);
        }
    }

    /**
     * Deletes the section file using the system file name
     *
     * @param systemFileName the system file name
     */
    @Override
    public void deleteSection(String systemFileName) {
        Path filePath = getFullPath(systemFileName);
        try {
            Files.delete(filePath);
            logger.info("Deleted file '{}' on delete section", filePath);
        } catch (IOException e) {
            handleException(e, filePath);
        }
    }

    /**
     * Deletes the section file using the system file name, if it is present
     *
     * @param systemFileName the system file name
     */
    @Override
    public void deleteSectionIfPresent(String systemFileName) {
        Path filePath = getFullPath(systemFileName);
        try {
            boolean exists = Files.deleteIfExists(filePath);
            if (exists) {
                logger.info("Deleted file '{}' on delete if present", filePath);
            } else {
                logger.info("File '{}' not found. Nothing to delete", filePath);
            }
        } catch (IOException e) {
            handleException(e, filePath);
        }
    }

    private void handleException(IOException e, Path filePath) {
        logger.error("Unable to remove stored section '{}'", filePath);
        throw new InvalidSystemStateException(e);
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
            throw new InvalidSystemStateException(e);
        }
    }
}
