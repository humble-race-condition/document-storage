package com.example.springbootdemoproject;


import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/** This class aims to implement the SAGA design pattern.
 * When a transaction in one system is finished, the other system may not end with a commit and the system
 * is left in an unfinished state. This record contains information about the actions that need to take place
 * in the secondary system. It records if the transaction was successful or not. It stores from which transaction group
 * the record is, meaning that multiple actions can be in the same transaction group.
 * If the transaction is not successful, then the record must be processed and the resource released if present.
 */
@Entity
@Table(name = "transaction_action_records")
public class TransactionActionRecord {
    @Id
    @UuidGenerator
    private UUID id;
    private String storageLocation;
    private ActionType actionType;
    private boolean committed;
    private boolean processed;
    private UUID transactionGroup;
    @ManyToOne
    @JoinColumn(name = "parent_record_id", nullable = true)
    private TransactionActionRecord parentRecord;
    @OneToMany(mappedBy = "parentRecord")
    private Set<TransactionActionRecord> childActionRecords;
    private LocalDateTime createdAt;
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

    public UUID getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(UUID transactionGroup) {
        this.transactionGroup = transactionGroup;
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
