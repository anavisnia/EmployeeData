package com.example.employeedata.helpers;

import org.elasticsearch.common.UUIDs;

public final class HelperFunctions {
    public static String generateId() {
        return UUIDs.randomBase64UUID();
    }
}
