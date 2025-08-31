package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.features.datarecords.requests.CreateDataRecordRequest;
import com.example.springbootdemoproject.features.datarecords.requests.UpdateDataRecordRequest;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
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
     * Gets all data record details filtered by the query parameters
     *
     * @return {@link DataRecordContainerResponse} the data record details
     */
    @GetMapping
    public ResponseEntity<DataRecordContainerResponse> getDataRecords(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        //ToDo fix request body. Make it a single unified request body
        DataRecordContainerResponse dataRecords = dataRecordService.getDataRecords(title, description, page, size, sort);
        return ResponseEntity.ok().body(dataRecords);
    }

    /**
     * Gets data record details by id
     *
     * @return {@link DataRecordDetail} the data record details
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataRecordDetail> getDataRecordById(@PathVariable int id) {
        DataRecordDetail dataRecord = dataRecordService.getDataRecordById(id);
        return ResponseEntity.ok().body(dataRecord);
    }

    /**
     * Creates a data record with the specified fields
     *
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
     *
     * @param id      of the data record
     * @param request {@link UpdateDataRecordRequest} The modified values
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataRecordDetail> updateDataRecord(@PathVariable int id, @Valid @RequestBody UpdateDataRecordRequest request) {
        DataRecordDetail dataRecord = dataRecordService.updateDataRecord(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }
}
