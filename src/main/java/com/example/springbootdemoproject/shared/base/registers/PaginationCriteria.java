package com.example.springbootdemoproject.shared.base.registers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public record PaginationCriteria(Integer page, Integer size, String[] sort) {
    public PaginationCriteria {
        page = Math.max(RegisterCriteriaParserImpl.PAGE, Optional.ofNullable(page).orElse(RegisterCriteriaParserImpl.PAGE));
        size = Math.max(RegisterCriteriaParserImpl.SIZE, Optional.ofNullable(size).orElse(RegisterCriteriaParserImpl.PAGE));
        sort = sort != null
                ? Arrays.stream(sort).filter(Objects::nonNull).toArray(String[]::new)
                : new String[0];
    }
}
