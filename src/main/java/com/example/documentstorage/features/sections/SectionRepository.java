package com.example.documentstorage.features.sections;

import com.example.documentstorage.entities.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<DataRecord, Integer> {
    @Query("""
                SELECT new com.example.documentstorage.features.sections.SectionDownloadData(s.id, s.fileName, s.storageLocation, s.contentType)
                FROM DataRecord d
                JOIN d.sections s WHERE d.id = :dataRecordId AND s.id = :sectionId
                """)
    Optional<SectionDownloadData> findByIdAndSectionId(int dataRecordId, int sectionId);
}
