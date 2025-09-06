package com.example.springbootdemoproject.shared.base.filestorage;

import com.example.springbootdemoproject.entities.TransactionActionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionActionRecordRepository extends JpaRepository<TransactionActionRecord, UUID> {
    @Query("""
            SELECT t FROM TransactionActionRecord t
            WHERE t.processed = false AND t.createdAt < :expirationTime
            ORDER BY t.createdAt ASC
            LIMIT 1000
            """)
    List<TransactionActionRecord> findFirstNotProcessed(@Param("expirationTime") LocalDateTime expirationTime);
}
