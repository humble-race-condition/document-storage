package com.example.documentstorage.features.sections;

import com.example.documentstorage.shared.base.models.responses.DataRecordDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/data-records/{dataRecordId}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    //ToDo Soft Delete on data records
    //ToDo unique for fields, and add a check
    // ToDo add flyway sometime, but for now, its easier this way
    @PostMapping
    public ResponseEntity<DataRecordDetail> uploadSection(@PathVariable int dataRecordId, @RequestParam("section") MultipartFile sectionFile) {
        DataRecordDetail recordDetail = sectionService.uploadSection(dataRecordId, sectionFile);
        return ResponseEntity.ok(recordDetail);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable int dataRecordId, @PathVariable int sectionId) {
        sectionService.deleteSection(dataRecordId, sectionId);
        return ResponseEntity.noContent().build();
    }
}
