package com.example.employeedata.controller.elastic;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeedata.document.*;
import com.example.employeedata.dto.ResultQueryDto;
import com.example.employeedata.helpers.Constants;
import com.example.employeedata.service.ESearchService;

import io.swagger.annotations.*;

@Api(tags = "Service to manage elasticsearch queries")
@RestController
@RequestMapping("/api/search")
public class ESearchController {
    private final ESearchService searchService;

    public ESearchController(ESearchService searchService) {
        this.searchService = searchService;
    }

    @ApiOperation(value = "Get all employees")
    @GetMapping("/allEmployees")
    public ResponseEntity<Iterable<EmployeeDoc>> getAllEmployees() {
        return new ResponseEntity<Iterable<EmployeeDoc>>(searchService.getAllEmployees(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all projects")
    @GetMapping("/allProjects")
    public ResponseEntity<Iterable<ProjectDoc>> getAllProjects() {
        return new ResponseEntity<Iterable<ProjectDoc>>(searchService.getAllProjects(), HttpStatus.OK);
    }

    @ApiOperation(value = "Search by query and fields")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "query",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Query to execute")
        })
    //post mapping is used to get search fields as an array
    @PostMapping(Constants.SEARCH_QUERY_FIELDS + "/{" + Constants.QUERY + "}")
    public ResponseEntity<ResultQueryDto> searchByFieldsQuery(@PathVariable String query, @RequestBody String[] fields) throws IOException {
        return new ResponseEntity<ResultQueryDto>(searchService.searchByQueryCustomFields(fields, query.trim().toLowerCase()), HttpStatus.OK);
    }

    @ApiOperation(value = "Search projects by query and fields")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "query",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Query to execute")
        })
    //post mapping is used to get search fields as an array
    @PostMapping(Constants.PROJECT + Constants.SEARCH_QUERY_FIELDS + "/{" + Constants.QUERY + "}")
    public ResponseEntity<ResultQueryDto> searchProjectsByFieldsQuery(@PathVariable String query, @RequestBody String[] fields) throws IOException {
        return new ResponseEntity<ResultQueryDto>(searchService.searchByQueryCustomFields(Constants.PROJECT, fields, query.trim().toLowerCase()), HttpStatus.OK);
    }

    @ApiOperation(value = "Search employees by query and fields")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "query",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Query to execute")
        })
    //post mapping is used to get search fields as an array
    @PostMapping(Constants.EMPLOYEE + Constants.SEARCH_QUERY_FIELDS + "/{" + Constants.QUERY + "}")
    public ResponseEntity<ResultQueryDto> searchEmployeesByFieldsQuery(@PathVariable String query, @RequestBody String[] fields) throws IOException {
        return new ResponseEntity<ResultQueryDto>(searchService.searchByQueryCustomFields(Constants.EMPLOYEE, fields, query.trim().toLowerCase()), HttpStatus.OK);
    }
}
