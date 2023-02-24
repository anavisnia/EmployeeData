package com.example.employeedata.helpers;

public final class Constants {
    public static final String EMPLOYEE = "employee";
    public static final String PROJECT = "project";
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


    public static final String[] EMPLOYEE_FILE_HEADERS = {"First name", "Last name", "Birth date", "Role", "Developer language", "Project ids"};
    public static final String[] PROJECT_FILE_HEADERS = {"Title", "Description", "Customer", "Team size", "Developer language", "Termination date", "Completion date"};

    public static final String EMPLOYEE_FILE_NAME = "EmployeeDataFor_";
    public static final String PROJECT_FILE_NAME = "ProjectDataFor_";
    public static final String DOWNLOAD_OCTET_STREAM = "application/octet-stream";
    public static final String ATTACHMENT_FILENAME = "attachment; filename=\"";

    public static final String REGEX_NAME = "^[a-zA-Z\\p{L}]+$";
    public static final String REGEX_TEXT_WITHOUT_SYMBOLS = "^[a-zA-Z0-9 \\p{L}]+$";
    public static final String REGEX_TEXT_WITH_SYMBOLS = "^[A-Za-z0-9- \\p{L},._-|]+$";

    public static final String[] EMPLOYEE_FIELDS = {"id", "firstName", "lastName", "role", "devLanguage", "projects", "birthDate"};
    public static final String[] EMPLOYEE_DB_FIELDS = {"id", "first_name", "last_name", "role", "dev_language", "birth_date"};
    public static final String[] PROJECT_FIELDS = {"id", "title", "description", "customer", "devLanguage", "terminationDate", "completionDate"};
    public static final String[] PROJECT_DB_FIELDS = {"id", "title", "description", "customer", "dev_language", "termination_date", "completion_date"};
}
