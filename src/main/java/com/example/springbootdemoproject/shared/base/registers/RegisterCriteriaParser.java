package com.example.springbootdemoproject.shared.base.registers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class RegisterCriteriaParser {
    /**
     * Parses all the pagination criteria
     *
     * @param paginationCriteria criteria for the pagination and sort order
     * @return a {@code Pageable}
     */
    public static Pageable parsePaginationCriteria(PaginationCriteria paginationCriteria) {
        int page = Math.max(paginationCriteria.page(), 0);
        int size = Math.max(paginationCriteria.size(), 1);
        String[] sort = Optional.ofNullable(paginationCriteria.sort()).orElse(new String[0]);

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortParam : sort) {
            String[] parts = sortParam.split(",");
            if (parts.length == 2) {
                orders.add(new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]));
            } else {
                orders.add(new Sort.Order(Sort.Direction.ASC, parts[0]));
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        return pageable;
    }

    /**
     * Parses all the filter criteria and their logical operator and returns a {@code Specification}
     *
     * @param filterCriteria the criteria by which to filter
     * @param <T>            type of the entity
     * @return a {@code Specification}
     */
    public static <T> Specification<T> parseFilterCriteria(FilterCriteria filterCriteria) {
        if (filterCriteria.filter() == null || filterCriteria.filter().length == 0) {
            return Specification.anyOf(new ArrayList<>());
        }

        String operator = Optional.ofNullable(filterCriteria.operator())
                .map(String::toUpperCase)
                .orElse("AND");
        List<Specification<T>> specifications = Arrays.stream(filterCriteria.filter())
                .map(s -> {
                    int indexOfFirstSeparator = s.indexOf(",");
                    String filterOperator = s.substring(0, indexOfFirstSeparator).toUpperCase();
                    int indexOfSecondSeparator = s.indexOf(",", indexOfFirstSeparator + 1);
                    String columnName = s.substring(indexOfFirstSeparator + 1, indexOfSecondSeparator);
                    String value = s.substring(indexOfSecondSeparator + 1);
                    Specification<T> specification = switch (filterOperator) {
                        case "EQUALS" -> SpecificationHandlers.equals(value, columnName);
                        case "STARTSWITH" -> SpecificationHandlers.startsWith(value, columnName);
                        case "CONTAINS" -> SpecificationHandlers.contains(value, columnName);
                        default -> null;
                    };

                    return specification;
                })
                .filter(Objects::nonNull)
                .toList();


        Specification<T> specification = switch (operator) {
            case "AND" -> Specification.allOf(specifications);
            case "OR" -> Specification.anyOf(specifications);
            default -> null;
        };

        return specification;
    }
}
