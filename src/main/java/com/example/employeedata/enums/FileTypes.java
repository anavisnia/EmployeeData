package com.example.employeedata.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum FileTypes {
    Xls("xls"),
    Xlsx("xlsx");

    public final String label;

    FileTypes(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static final Integer size() {
        return Role.values().length;
    }

    public static final String[] labels() {
        return Arrays.stream(FileTypes.values())
        .map(Object::toString)
        .collect(Collectors.toList())
        .toArray(new String[FileTypes.size()]);
    }
}
