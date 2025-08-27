package com.example.springbootdemoproject.features.sections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/data-records/{id}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public String test1() {
        return "test 1";
    }
    //ToDo Soft Delete on datarecords
    //ToDo unique for fields, and add a check
    // ToDo add flyway sometime, but for now, its easier this way
    @PostMapping
    public ResponseEntity<String> uploadSection(@PathVariable int id, @RequestPart("section") MultipartFile section, @RequestPart("test")Demo Demo) {
        sectionService.uploadSection(section);
        return ResponseEntity.ok("uploaded");
    }
}
