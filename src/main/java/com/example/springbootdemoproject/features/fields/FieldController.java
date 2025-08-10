package com.example.springbootdemoproject.features.fields;

import com.example.springbootdemoproject.features.fields.requests.RemoveFieldsRequest;
import com.example.springbootdemoproject.features.fields.requests.UpdateFieldsRequest;
import com.example.springbootdemoproject.shared.base.models.responses.DataRecordDetail;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-records")
public class FieldController {
    private final FieldService fieldService;

    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    /**
     * Adds new fields or overrides existing fields
     * @param id the data record identifier
     * @param request {@link UpdateFieldsRequest} the data record updated fields
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @PutMapping("/{id}/fields")
    public ResponseEntity<DataRecordDetail> addFields(@PathVariable int id, @Valid @RequestBody UpdateFieldsRequest request) {
        DataRecordDetail dataRecord = fieldService.updateDataRecordFields(id, request);
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
        DataRecordDetail dataRecord = fieldService.removeDataRecordFields(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }
}
