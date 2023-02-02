package com.example.employeedata.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.UUIDs;

public final class HelperFunctions {
    public static String generateId() {
        return UUIDs.randomBase64UUID();
    }

    public static List<Long> getListOfLongValuesFromString(String str) {
        Set<Long> values = new HashSet<>();

        if (!str.isBlank()) {
            String[] seperatedNumbers = str.trim().split(", ");

            for (String string : seperatedNumbers) {
                values.add(Long.parseLong(string.replace(".0", "")));
            }
        }

        return new ArrayList(values);
    }

}
