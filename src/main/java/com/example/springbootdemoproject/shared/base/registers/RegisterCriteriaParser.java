package com.example.springbootdemoproject.shared.base.registers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RegisterCriteriaParser {

    /**
     * Parses all the pagination criteria
     *
     * @param paginationCriteria criteria for the pagination and sort order
     * @return a {@code Pageable}
     */
    Pageable parsePaginationCriteria(PaginationCriteria paginationCriteria);

    /**
     * Parses all the filter criteria and their logical operator and returns a {@code Specification}
     *
     * @param filterCriteria the criteria by which to filter
     * @param <T>            type of the entity
     * @return a {@code Specification}
     */
    <T> Specification<T> parseFilterCriteria(FilterCriteria filterCriteria);
}
