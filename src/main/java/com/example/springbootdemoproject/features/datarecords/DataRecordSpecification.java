package com.example.springbootdemoproject.features.datarecords;

import com.example.springbootdemoproject.entities.DataRecord;
import org.springframework.data.jpa.domain.Specification;

public class DataRecordSpecification {
    public static Specification<DataRecord> hasTitle(String title) {
        return (root, query, cb) -> "".equals(title) ? null : cb.equal(root.get("title"), title);
    }

    public static Specification<DataRecord> hasDescription(String description) {
        return (root, query, cb) -> "".equals(description) ? null : cb.equal(root.get("description"), description);
    }
}
