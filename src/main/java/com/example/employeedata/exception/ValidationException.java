package com.example.employeedata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private String customMessage;

    public ValidationException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Validation error for %s in %s property. Was enterred: '%s'.", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ValidationException(String resourceName, String fieldName, Object fieldValue, String customMessage) {
        super(String.format("Validation error for %s in %s property. Was enterred: '%s'. %s.", resourceName, fieldName, fieldValue, customMessage));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getCustomMessage() {
        return customMessage;
    }

}
