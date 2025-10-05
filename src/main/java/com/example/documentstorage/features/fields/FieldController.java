package com.example.documentstorage.features.fields;

import com.example.documentstorage.features.fields.requests.RemoveFieldsRequest;
import com.example.documentstorage.features.fields.requests.UpdateFieldsRequest;
import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import com.example.documentstorage.shared.base.problemdetail.ProblemDetailMapper;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-records/{id}/fields")
public class FieldController {
    private final FieldService fieldService;
    private final ProblemDetailMapper problemDetailMapper;

    public FieldController(FieldService fieldService, ProblemDetailMapper problemDetailMapper) {
        this.fieldService = fieldService;
        this.problemDetailMapper = problemDetailMapper;
    }

    /**
     * Adds new fields or overrides existing fields
     *
     * @param id      the data record identifier
     * @param request {@link UpdateFieldsRequest} the data record updated fields
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @PutMapping
    public ResponseEntity<?> updateFields(@PathVariable int id, @Valid @RequestBody UpdateFieldsRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResponseEntity<ProblemDetail> responseEntity = problemDetailMapper.handleBindingResult(bindingResult);
            return responseEntity;
        }

        DataRecordDetail dataRecord = fieldService.updateDataRecordFields(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }

    /**
     * Remove the selected data record fields
     *
     * @param id      of the data record
     * @param request {@link RemoveFieldsRequest} The fields to be removed
     * @return {@link DataRecordDetail} the updated DataRecordDetails
     */
    @DeleteMapping
    public ResponseEntity<?> removeFields(@PathVariable int id, @Valid @RequestBody RemoveFieldsRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResponseEntity<ProblemDetail> responseEntity = problemDetailMapper.handleBindingResult(bindingResult);
            return responseEntity;
        }

        DataRecordDetail dataRecord = fieldService.removeDataRecordFields(id, request);
        return ResponseEntity.ok().body(dataRecord);
    }
}
