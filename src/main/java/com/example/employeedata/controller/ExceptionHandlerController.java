package com.example.employeedata.controller;

import java.util.*;
import java.util.stream.*;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.employeedata.exception.*;
import com.example.employeedata.helpers.DateTimeHelpers;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    private final String dateTimeFormat = "yyyy-MM-dd hh:mm:ss";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> respBody = new LinkedHashMap<>();
        respBody.put("timeStamp", DateTimeHelpers.getLocalDateTimeNow(dateTimeFormat));
        respBody.put("message", ex.getMessage());

        return new ResponseEntity<>(respBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Object> handleCustomValidationException(
        CustomValidationException ex, WebRequest request) {

        Map<String, Object> respBody = new LinkedHashMap<>();
        respBody.put("timeStamp", DateTimeHelpers.getLocalDateTimeNow(dateTimeFormat));
        respBody.put("message", ex.getMessage());

        return new ResponseEntity<>(respBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
        ConstraintViolationException ex, WebRequest request) {

        Map<String, Object> respBody = new LinkedHashMap<>();
        respBody.put("timeStamp", DateTimeHelpers.getLocalDateTimeNow(dateTimeFormat));
        respBody.put("message", ex.getMessage());

        return new ResponseEntity<>(respBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerErrorException(
        Exception ex) {
        
        Map<String, Object> respBody = new LinkedHashMap<>();
        respBody.put("timeStamp", DateTimeHelpers.getLocalDateTimeNow(dateTimeFormat));
        respBody.put("message", "Internal Server Error");

        return new ResponseEntity<>(respBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<Object> handleDataAccessException(
        DataAccessResourceFailureException ex) {
        
        Map<String, Object> respBody = new LinkedHashMap<>();
        respBody.put("timeStamp", DateTimeHelpers.getLocalDateTimeNow(dateTimeFormat));
        respBody.put("message", "Data Access Resource Failure Error, Initial cause: " + ex.initCause(ex));

        return new ResponseEntity<>(respBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, 
        HttpStatus status, WebRequest request) {

        Map<String, Object> respBody = new LinkedHashMap<>();
        respBody.put("timeStamp", DateTimeHelpers.getLocalDateTimeNow(dateTimeFormat));
        respBody.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        respBody.put("errors", errors);

        return new ResponseEntity<>(respBody, HttpStatus.BAD_REQUEST);
    }
    
}
