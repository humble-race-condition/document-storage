package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, Integer> {
}
