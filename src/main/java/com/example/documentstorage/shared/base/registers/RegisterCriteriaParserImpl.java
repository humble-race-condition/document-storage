package com.example.documentstorage.shared.base.registers;

import com.example.documentstorage.shared.base.exceptions.InvalidClientInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterCriteriaParserImpl implements RegisterCriteriaParser {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCriteriaParserImpl.class);

    private static final String SEPARATOR = ":";
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final int PAGE = 0;
    public static final int SIZE = 1;

    public RegisterCriteriaParserImpl() {
    }

    /**
     * Parses all the pagination criteria
     *
     * @param paginationCriteria criteria for the pagination and sort order
     * @return a {@code Pageable}
     */
    @Override
    public Pageable parsePaginationCriteria(PaginationCriteria paginationCriteria) {
        int page = Math.max(RegisterCriteriaParserImpl.PAGE, Optional.ofNullable(paginationCriteria.page()).orElse(RegisterCriteriaParserImpl.PAGE));
        int size = Math.max(RegisterCriteriaParserImpl.SIZE, Optional.ofNullable(paginationCriteria.size()).orElse(RegisterCriteriaParserImpl.PAGE));
        String[] sort = validateSortFormat(paginationCriteria.sort());

        List<Sort.Order> sortOrders = Arrays.stream(sort)
                .map(s -> s.split(SEPARATOR))
                .sorted(Comparator.comparing(s -> Integer.parseInt(s[0])))
                .map(s -> new Sort.Order(Sort.Direction.fromString(s[2]), s[1]))
                .toList();

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));

        return pageable;
    }

    /**
     * Parses all the filter criteria and their logical operator and returns a {@code Specification}
     *
     * @param filterCriteria the criteria by which to filter
     * @param <T>            type of the entity
     * @return a {@code Specification}
     */
    @Override
    public <T> Specification<T> parseFilterCriteria(FilterCriteria filterCriteria) {
        String[] filter = validateFilterCriteriaFormat(filterCriteria.filter());
        String operator = Optional.ofNullable(filterCriteria.operator())
                .map(String::toUpperCase)
                .orElse(RegisterCriteriaParserImpl.OR);

        if (filter.length == 0) {
            return Specification.anyOf(new ArrayList<>());
        }

        List<Specification<T>> specifications = Arrays.stream(filter)
                .map(s -> {
                    String[] splitFilter = s.split(SEPARATOR);
                    String filterOperator = splitFilter[0].toUpperCase();
                    String columnName = splitFilter[1];
                    String value = splitFilter[2];
                    Specification<T> specification = switch (filterOperator) {
                        case "EQUALS" -> SpecificationHandlers.equals(columnName, value);
                        case "STARTSWITH" -> SpecificationHandlers.startsWith(columnName, value);
                        case "CONTAINS" -> SpecificationHandlers.contains(columnName, value);
                        default -> null;
                    };

                    return specification;
                })
                .filter(Objects::nonNull)
                .toList();

        Specification<T> specification = AND.equals(operator)
                ? Specification.allOf(specifications)
                : Specification.anyOf(specifications);

        return specification;
    }

    private String[] validateSortFormat(String[] sorts) {
        String[] validatedSorts = sorts != null
                ? Arrays.stream(sorts).filter(Objects::nonNull).toArray(String[]::new)
                : new String[0];

        for (String sort : validatedSorts) {
            if (sort == null) {
                logger.error("The sort order is null");
                throw new InvalidClientInputException("features.datarecords.on.datarecord.get.all.sort.is.null");
            }

            String[] splitSort = sort.split(SEPARATOR);
            if (splitSort.length != 3) {
                throwSortException(sort);
            }

            try {
                Integer.parseInt(splitSort[0]);
            } catch (Exception ex) {
                throwSortException(sort);
            }

            if (!ASC.equalsIgnoreCase(splitSort[2]) && !DESC.equalsIgnoreCase(splitSort[2])) {
                throwSortException(sort);
            }
        }

        return validatedSorts;
    }

    private String[] validateFilterCriteriaFormat(String[] filters) {
        String[] validatedFilters = filters != null
                ? Arrays.stream(filters).filter(Objects::nonNull).toArray(String[]::new)
                : new String[0];

        for (String filter : validatedFilters) {
            if (filter == null) {
                logger.error("The filter is null");
                throw new InvalidClientInputException("features.datarecords.on.datarecord.get.all.filter.is.null");
            }

            String[] splitFilter = filter.split(SEPARATOR);
            if (splitFilter.length != 3) {
                logger.error("The filter '{}' does not have a valid format", filter);
                throw new InvalidClientInputException("features.datarecords.on.datarecord.get.all.filter.not.valid", filter);
            }
        }

        return validatedFilters;
    }

    private void throwSortException(String sort) {
        logger.error("The sort '{}' does not have a valid format", sort);
        throw new InvalidClientInputException("features.datarecords.on.datarecord.get.all.sort.not.valid", sort);
    }
}
