package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateFieldsRequest;
import com.example.springbootdemoproject.features.datarecords.responses.DataRecordDetail;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-records")
public class DataRecordController {

    private final DataRecordService dataRecordService;

    public DataRecordController(DataRecordService dataRecordService) {
        this.dataRecordService = dataRecordService;
    }

    @PostMapping
    public ResponseEntity<DataRecordDetail> createDataRecord(@Valid @RequestBody CreateDataRecordRequest request) {
        DataRecordDetail dataRecord = dataRecordService.createDataRecord(request);
        return ResponseEntity.ok().body(dataRecord);
    }

    @PutMapping("{id}")
    public ResponseEntity<DataRecordDetail> updateDataRecord(@PathVariable int id, @Valid @RequestBody UpdateDataRecordRequest request) {
        DataRecordDetail dataRecord = dataRecordService.updateDataRecord(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }

    /**
     * Adds new fields or overrides existing fields
     * @param id the data record identifier
     * @param request the data record updated fields
     * @return the updated data record
     */
    @PutMapping("/{id}/fields")
    public ResponseEntity<DataRecordDetail> addFields(@PathVariable int id, @Valid @RequestBody UpdateFieldsRequest request) {
        DataRecordDetail dataRecord = dataRecordService.updateDataRecordFields(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }

    @DeleteMapping("/{id}/fields")
    public ResponseEntity<DataRecordDetail> removeFields(@PathVariable int id, @Valid @RequestBody UpdateFieldsRequest request) {
//        DataRecordDetail dataRecord = dataRecordService.updateDataRecord(id, request);
        return ResponseEntity.ok().body(null);
    }
}
