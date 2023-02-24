package com.example.employeedata.helpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;

import com.example.employeedata.exception.ResourceNotFoundException;

public final class ESearchQuery {
    private final static String resourceName = "search field";
    
    public static String createMultiIndexMatchBody( String[] searchFields, String query) {
        if (!checkExistenceOfFields(searchFields)) {
            throw new ResourceNotFoundException(resourceName,"query fields", searchFields);
        }

        return "{\n" +
                "\"from\": 0,\n" +
                "\"size\": 100,\n" +
                "\"track_total_hits\": true,\n" +
                "\"sort\" : {\n" +
                "      \"id\": {\"order\": \"asc\"}\n" +
                "      },\n" +
                "  \"query\": {\n" +
                "    \"query_string\" : {\n" +
                "      \"query\":      \"*" + query + "*\",\n" +
                "      \"fields\":     " + new JSONArray(Arrays.asList(searchFields)) + ",\n" +
                "      \"default_operator\": \"AND\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"highlight\": {\n" +
                "    \"fields\": {\n" +
                "      \"*\": {}\n" +
                "    },\n" +
                "    \"require_field_match\": true\n" +
                " }\n" +
                "}";
    }

    public static String createSearchURI(
            String elasticSearchURI,
            String elasticSearchIndex,
            String elasticSearchSearch) {
        return elasticSearchURI + elasticSearchIndex + elasticSearchSearch;
    }

    private static boolean checkExistenceOfFields(String[] fields) {
        Set<String> staticFields = getExistingFields();

        return staticFields.containsAll(Arrays.asList(fields));
    }

    private static Set<String> getExistingFields() {
        Set<String> staticFields = new HashSet<>();
        Collections.addAll(staticFields, Constants.EMPLOYEE_FIELDS);
        Collections.addAll(staticFields, Constants.PROJECT_FIELDS);

        return staticFields;
    }
}
