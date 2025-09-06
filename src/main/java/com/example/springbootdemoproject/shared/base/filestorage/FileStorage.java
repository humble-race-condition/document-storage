package com.example.springbootdemoproject.shared.base.filestorage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    /**
     * Stores the section file using the system file name
     *
     * @param sectionFile    the file to be stored
     * @param systemFileName the system file name
     */
    void storeSection(MultipartFile sectionFile, String systemFileName);

    /**
     * Generates the system file name based on the original file name
     *
     * @param originalFileName the original file name
     * @return the system file name
     */
    String generateSystemFileName(String originalFileName);

    /**
     * Deletes the section file using the system file name
     *
     * @param systemFileName the system file name
     */
    void deleteSection(String systemFileName);

    /**
     * Deletes the section file using the system file name, if it is present
     *
     * @param systemFileName the system file name
     */
    void deleteSectionIfPresent(String systemFileName);
}
