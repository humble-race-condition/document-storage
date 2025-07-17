package com.example.springbootdemoproject;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public UUID save(@RequestBody TransactionActionRecord request) {
        TransactionActionRecord transactionActionRecord = new TransactionActionRecord();
        transactionActionRecord.setStorageLocation(request.getStorageLocation());
        transactionActionRecord.setActionType(request.getActionType());
        transactionActionRecord.setCommitted(false);
        transactionActionRecord.setProcessed(false);

        TransactionActionRecord childRecord = new TransactionActionRecord();
        childRecord.setStorageLocation(request.getStorageLocation());
        childRecord.setActionType(ActionType.DELETE);
        childRecord.setCommitted(false);
        childRecord.setProcessed(false);
        childRecord.setParentRecord(transactionActionRecord);

        transactionActionRecord.setChildActionRecords(Set.of(childRecord));

        repository.save(childRecord);
        
        return transactionActionRecord.getId();
    }

    @Transactional
    @DeleteMapping
    public void delete() {
        List<TransactionActionRecord> all = repository.findAll();
        Optional<TransactionActionRecord> record = all.stream().filter(x -> x.getParentRecord() == null).findFirst();
        if (record.isPresent()) {
            TransactionActionRecord transactionActionRecord = record.get();
//            transactionActionRecord.getChildActionRecords().forEach(x -> x.setParentRecord(null));
//            transactionActionRecord.setChildActionRecords(Set.of());

            repository.deleteById(transactionActionRecord.getId());
        }
    }
}
