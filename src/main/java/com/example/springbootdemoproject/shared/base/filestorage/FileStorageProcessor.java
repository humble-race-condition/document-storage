package com.example.springbootdemoproject.shared.base.filestorage;

import com.example.springbootdemoproject.entities.TransactionActionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component()
public class FileStorageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageProcessor.class);
    private final TransactionActionRecordRepository repository;
    private final FileStorage fileStorage;

    public FileStorageProcessor(TransactionActionRecordRepository repository, FileStorage fileStorage) {
        this.repository = repository;
        this.fileStorage = fileStorage;
    }

    /**
     * Processes the transaction actions that have been recorded.
     * If the {@link ActionType} is CREATE and the transaction action has been commited, then do nothing.
     * If the {@link ActionType} is CREATE and the transaction action has not been commited
     * If the {@link ActionType} is DELETE and the transaction action has been commited, then the document needs to be
     * removed.
     * If the {@link ActionType} is DELETE and the transaction action has not been commited, then do nothing.
     */
    @Scheduled(cron = "${document.storage.process.transactions.cron}")
    public synchronized void processTransactions() {
        logger.info("Start processing transactions");
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(1);
        List<TransactionActionRecord> transactionActions = repository.findFirstNotProcessed(expirationTime);
        while (!transactionActions.isEmpty()) {
            for (TransactionActionRecord transactionActionRecord : transactionActions) {
                handleTransactionAction(transactionActionRecord);
            }

            repository.saveAllAndFlush(transactionActions);
            transactionActions = repository.findFirstNotProcessed(expirationTime);
        }
    }

    private void handleTransactionAction(TransactionActionRecord record) {
        record.setProcessed(true);

        if (ActionType.CREATE.equals(record.getActionType()) && !record.isCommitted()) {
            fileStorage.deleteSection(record.getStorageLocation());
        } else if (ActionType.DELETE.equals(record.getActionType()) && record.isCommitted()) {
            fileStorage.deleteSection(record.getStorageLocation());
        }

        logger.info("Processing transaction record {}", record.getId());
    }
}
