package com.example.springbootdemoproject.shared.base.registers;

import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class SpecificationHandlers {
    /**
     * Creates a specification that matches if the column content is equal to the value
     * @param value the value that is filtered
     * @param column the colum to which the value is compared
     * @return the specification for the given type
     * @param <T> the type of the entity
     */
    public static <T> Specification<T> equals(String value, String column) {
        String comparedValue = Optional.ofNullable(value).orElse("");
        return (root, query, cb) -> comparedValue.isEmpty() ? null : cb.equal(root.get(column), comparedValue);
    }

    /**
     * Creates a specification that matches if the column content is starts with to the value
     * @param value the value that is filtered
     * @param column the colum to which the value is compared
     * @return the specification for the given type
     * @param <T> the type of the entity
     */
    public static <T> Specification<T> startsWith(String value, String column) {
        String comparedValue = Optional.ofNullable(value).orElse("");
        String formattedValue = "%" + comparedValue;
        return (root, query, cb) -> comparedValue.isEmpty() ? null : cb.like(root.get(column), formattedValue);
    }

    /**
     * Creates a specification that matches if the column content contains the value
     * @param value the value that is filtered
     * @param column the colum to which the value is compared
     * @return the specification for the given type
     * @param <T> the type of the entity
     */
    public static <T> Specification<T> contains(String value, String column) {
        String comparedValue = Optional.ofNullable(value).orElse("");
        String formattedValue = "%" + comparedValue + "%";
        return (root, query, cb) -> comparedValue.isEmpty() ? null : cb.like(root.get(column), formattedValue);
    }
}
