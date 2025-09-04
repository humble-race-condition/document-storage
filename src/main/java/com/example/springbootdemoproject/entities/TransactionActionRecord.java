package com.example.springbootdemoproject.entities;


import com.example.springbootdemoproject.shared.base.trasnasctionactions.ActionType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * This class aims to implement the SAGA design pattern.
 * When a transaction in one system is finished, the other system may not end with a commit and the system
 * is left in an unfinished state. This record contains information about the actions that need to take place
 * in the secondary system. It records if the transaction was successful or not. It stores from which transaction group
 * the record is, meaning that multiple actions can be in the same transaction group.
 * If the transaction is not successful, then the record must be processed and the resource released if present.
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "transaction_action_records")
public class TransactionActionRecord {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "storage_location")
    private String storageLocation;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private boolean committed;

    private boolean processed;

    @ManyToOne(cascade = {})
    @JoinColumn(name = "parent_record_id", nullable = true)
    private TransactionActionRecord parentRecord;

    @OneToMany(mappedBy = "parentRecord", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TransactionActionRecord> childActionRecords;

    @Version
    private long version;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public TransactionActionRecord getParentRecord() {
        return parentRecord;
    }

    public void setParentRecord(TransactionActionRecord parentRecord) {
        this.parentRecord = parentRecord;
    }

    public Set<TransactionActionRecord> getChildActionRecords() {
        return childActionRecords;
    }

    public void setChildActionRecords(Set<TransactionActionRecord> childActionRecords) {
        this.childActionRecords = childActionRecords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
