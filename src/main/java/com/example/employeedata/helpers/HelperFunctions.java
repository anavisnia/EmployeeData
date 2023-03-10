package com.example.employeedata.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.common.UUIDs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class HelperFunctions {
    public static String generateId() {
        return UUIDs.randomBase64UUID();
    }

    public static Collection<Long> getListOfLongValuesFromString(String str) {
        if (!str.isBlank()) {
            return Stream.of(str.trim().split(", "))
                .map(string -> Long.parseLong(string.replace(".0", "")))
                .collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    public static Integer checkPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            return 10;
        } else if (pageSize > 500) {
            return 500;
        }

        return pageSize;
    }

    public static String checkFieldSorting(String[] entityFields, Integer sortBy) {
        if (sortBy == null ||
                sortBy < 0 ||
                sortBy > entityFields.length - 1) {
            return "id"; //default
        }

        return entityFields[sortBy];
    }

    public static Pageable returnPageableWithSorting(Integer pageNumber, Integer pageSize, String query, String isAsc) {
        boolean order = Boolean.parseBoolean(isAsc);

        if (query == null || query.isBlank()) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(order ? Sort.Direction.ASC : Sort.Direction.DESC));
        } else if (isAsc == null || isAsc.isBlank()) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(query));
        } else if (order) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(query).ascending());
        } else {
            return PageRequest.of(pageNumber, pageSize, Sort.by(query).descending());
        }
    }

}
