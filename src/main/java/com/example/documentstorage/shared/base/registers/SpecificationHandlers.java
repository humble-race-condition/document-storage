package com.example.documentstorage.shared.base.registers;

import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class SpecificationHandlers {
    /**
     * Creates a specification that matches if the column content is equal to the value
     *
     * @param column the colum to which the value is compared
     * @param value  the value that is filtered
     * @param <T>    the type of the entity
     * @return the specification for the given type
     */
    public static <T> Specification<T> equals(String column, String value) {
        String comparedValue = Optional.ofNullable(value).orElse("");
        return (root, query, cb) -> comparedValue.isEmpty() ? null : cb.equal(root.get(column), comparedValue);
    }

    /**
     * Creates a specification that matches if the column content is starts with to the value
     *
     * @param column the colum to which the value is compared
     * @param value  the value that is filtered
     * @param <T>    the type of the entity
     * @return the specification for the given type
     */
    public static <T> Specification<T> startsWith(String column, String value) {
        String comparedValue = Optional.ofNullable(value).orElse("");
        String formattedValue = comparedValue + "%";
        return (root, query, cb) -> comparedValue.isEmpty() ? null : cb.like(root.get(column), formattedValue);
    }

    /**
     * Creates a specification that matches if the column content contains the value
     *
     * @param column the colum to which the value is compared
     * @param value  the value that is filtered
     * @param <T>    the type of the entity
     * @return the specification for the given type
     */
    public static <T> Specification<T> contains(String column, String value) {
        String comparedValue = Optional.ofNullable(value).orElse("");
        String formattedValue = "%" + comparedValue + "%";
        return (root, query, cb) -> comparedValue.isEmpty() ? null : cb.like(root.get(column), formattedValue);
    }
}
