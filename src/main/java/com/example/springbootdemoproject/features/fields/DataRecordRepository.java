package com.example.springbootdemoproject.features.fields;

import com.example.springbootdemoproject.entities.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("fieldDataRecordRepository")
public interface DataRecordRepository extends JpaRepository<DataRecord, Integer> {
}
