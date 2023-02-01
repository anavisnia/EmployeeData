package com.example.employeedata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class CustomValidationException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private String customMessage;

    public CustomValidationException(String fieldName, String customMessage) {
        super(String.format("Validation error for %s. %s.", fieldName, customMessage));
        this.fieldName = fieldName;
    }

    public CustomValidationException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Validation error for %s in %s property. Was enterred: '%s'.", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public CustomValidationException(String resourceName, String fieldName, Object fieldValue, String customMessage) {
        super(String.format("Validation error for %s in %s property. Was enterred: '%s'. %s.", resourceName, fieldName, fieldValue, customMessage));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
