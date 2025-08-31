package com.example.springbootdemoproject.shared.base.models.responses;

import java.util.List;

public record DataRecordDetail(
        int id,
        String title,
        String description,
        List<FieldDetail> fields,
        List<SectionDetail> sections) {

    public static DataRecordDetail withFields(int id,
                                              String title,
                                              String description,
                                              List<FieldDetail> fields) {
        return new DataRecordDetail(id, title, description, fields, List.of());
    }

    public static DataRecordDetail withSections(int id,
                                                String title,
                                                String description,
                                                List<SectionDetail> sections) {
        return new DataRecordDetail(id, title, description, List.of(), sections);
    }

    public static DataRecordDetail fromBase(int id,
                                            String title,
                                            String description) {
        return new DataRecordDetail(id, title, description, List.of(), List.of());
    }
}
