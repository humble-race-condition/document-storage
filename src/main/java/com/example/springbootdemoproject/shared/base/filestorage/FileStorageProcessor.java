package com.example.springbootdemoproject.shared.base.filestorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileStorageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageProcessor.class);

    /**
     * Processes the transaction actions that have been recorded.
     * If the {@link ActionType} is CREATE and the transaction action has been commited, then do nothing.
     * If the {@link ActionType} is CREATE and the transaction action has not been commited
     * If the {@link ActionType} is DELETE and the transaction action has been commited, then the document needs to be
     * removed.
     * If the {@link ActionType} is DELETE and the transaction action has not been commited, then do nothing.
     */
    @Scheduled(cron = "${document.storage.process.transactions.cron}")
    public void processTransactions() {
        logger.info("Start processing transactions");

        System.out.println("Hello there");
    }
}
