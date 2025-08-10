package com.example.springbootdemoproject.features.sections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data-records/{id}/sections")
public class SectionController {

    @GetMapping
    public String test1() {
        return "test 1";
    }

    @PostMapping
    public String test2() {
        return "test 2";
    }
}
