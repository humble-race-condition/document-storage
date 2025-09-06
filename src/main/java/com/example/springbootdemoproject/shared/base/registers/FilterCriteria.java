package com.example.springbootdemoproject.shared.base.registers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public record FilterCriteria(String[] filter, String operator) {
    public FilterCriteria {
        filter = filter != null
                ? Arrays.stream(filter).filter(Objects::nonNull).toArray(String[]::new)
                : new String[0];
        operator = Optional.ofNullable(operator)
                .map(String::toUpperCase)
                .orElse(RegisterCriteriaParserImpl.OR);
    }
}
