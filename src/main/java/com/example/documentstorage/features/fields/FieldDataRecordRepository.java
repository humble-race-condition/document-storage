package com.example.documentstorage.features.fields;

import com.example.documentstorage.entities.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldDataRecordRepository extends JpaRepository<DataRecord, Integer> {
}
