package com.example.employeedata.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.common.UUIDs;

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

}
