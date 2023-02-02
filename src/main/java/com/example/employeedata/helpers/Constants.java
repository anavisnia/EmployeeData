package com.example.employeedata.helpers;

public final class Constants {
    public static final String EMPLOYEE = "employee";
    public static final String PROJECT = "project";
    public static final String ENTITY_MODIFICATION_DATE = "modificationDate";
    public static final String SEARCH_QUERY_FIELDS = "/query_fields";
    public static final String QUERY = "query";
    public static final String HITS = "hits";
    public static final String TOOK = "took";
    public static final String TOTAL_HITS = "total";
    public static final String VALUE = "value";
    public static final String CONTENT_ACCEPT = "Accept";
    public static final String APP_TYPE = "application/json";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String UTF8_ENCODING = "UTF-8";
    public static final Integer MILLISECONDS = 1000;

    public static final Integer ALLOWED_EMPTY_FILEDS_EMPLOYEE = 1;

    public static final String USER_DOCUMENTS_PATH = System.getProperty("user.home") + "/Documents";
    public static final String[] EMPLOYEE_FILE_HEADERS = {"First name", "Last name", "Birth date", "Role", "Develoepr language", "Project ids"};
}
