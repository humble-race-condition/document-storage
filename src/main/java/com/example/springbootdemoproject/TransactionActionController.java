package com.example.springbootdemoproject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionActionController {
    private final TransactionActionRecordRepository repository;

    public TransactionActionController(TransactionActionRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionActionRecord> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public TransactionActionRecord save(@RequestBody TransactionActionRecord request) {
        TransactionActionRecord transactionActionRecord = new TransactionActionRecord();
        transactionActionRecord.setStorageLocation(request.getStorageLocation());
        transactionActionRecord.setActionType(request.getActionType());
        transactionActionRecord.setCommitted(false);
        transactionActionRecord.setProcessed(false);
        transactionActionRecord.setTransactionGroup(null);

        
        return repository.save(transactionActionRecord);
    }
}
