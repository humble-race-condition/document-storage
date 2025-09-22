package com.example.documentstorage.features.sections;

import com.example.documentstorage.entities.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<DataRecord, Integer> {
}
