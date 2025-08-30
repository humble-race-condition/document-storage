package com.example.springbootdemoproject.features.sections;

import com.example.springbootdemoproject.entities.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<DataRecord, Integer> {
}
