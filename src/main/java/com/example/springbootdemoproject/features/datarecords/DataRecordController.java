package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.RemoveFieldsRequest;
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

    /**
     * Creates a data record with the specified fields
     * @param request {@link CreateDataRecordRequest} The request for the data record
     * @return {@link DataRecordDetail} the data record details
     */
    @PostMapping
    public ResponseEntity<DataRecordDetail> createDataRecord(@Valid @RequestBody CreateDataRecordRequest request) {
        DataRecordDetail dataRecord = dataRecordService.createDataRecord(request);
        return ResponseEntity.ok().body(dataRecord);
    }

    /**
     * Updated the data record
     * @param id of the data record
     * @param request {@link UpdateDataRecordRequest} The modified values
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @PutMapping("{id}")
    public ResponseEntity<DataRecordDetail> updateDataRecord(@PathVariable int id, @Valid @RequestBody UpdateDataRecordRequest request) {
        DataRecordDetail dataRecord = dataRecordService.updateDataRecord(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }

    /**
     * Adds new fields or overrides existing fields
     * @param id the data record identifier
     * @param request {@link UpdateFieldsRequest} the data record updated fields
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @PutMapping("/{id}/fields")
    public ResponseEntity<DataRecordDetail> addFields(@PathVariable int id, @Valid @RequestBody UpdateFieldsRequest request) {
        DataRecordDetail dataRecord = dataRecordService.updateDataRecordFields(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }

    /**
     * Remove the selected data record fields
     * @param id of the data record
     * @param request {@link RemoveFieldsRequest} The fields to be removed
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @DeleteMapping("/{id}/fields")
    public ResponseEntity<DataRecordDetail> removeFields(@PathVariable int id, @Valid @RequestBody RemoveFieldsRequest request) {
        DataRecordDetail dataRecord = dataRecordService.removeDataRecord(id, request);
        return ResponseEntity.ok().body(null);
    }
}
