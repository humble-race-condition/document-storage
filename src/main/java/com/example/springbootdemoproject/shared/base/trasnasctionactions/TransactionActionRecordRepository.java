package com.example.springbootdemoproject.shared.base.trasnasctionactions;

import com.example.springbootdemoproject.entities.TransactionActionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionActionRecordRepository extends JpaRepository<TransactionActionRecord, UUID> {
}
