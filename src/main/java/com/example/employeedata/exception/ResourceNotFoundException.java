package com.example.employeedata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private String message;

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s not found.", resourceName));
        this.resourceName = resourceName;
        this.message = String.format("%s not found.", resourceName);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'.", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.message = String.format("%s not found with %s : '%s'.", resourceName, fieldName, fieldValue);
    }
}
