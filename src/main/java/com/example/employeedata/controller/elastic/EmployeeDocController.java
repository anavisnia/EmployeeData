package com.example.employeedata.controller.elastic;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.document.EmployeeDoc;
import com.example.employeedata.dto.*;
import com.example.employeedata.service.EmployeeDocService;

import io.swagger.annotations.*;

@Api(value = "Service to manage elasticsearch employee queries")
@RestController
@RequestMapping("api/employees/elastic")
public class EmployeeDocController {
    private final EmployeeDocService employeeDocService;

    public EmployeeDocController(EmployeeDocService employeeDocService) {
        this.employeeDocService = employeeDocService;
    }

    @ApiOperation(value = "Get all employees")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDoc>> getAllEmployees() {
        return new ResponseEntity<>(employeeDocService.getAllEmployees(), HttpStatus.OK);
    }

    @ApiOperation(value = "Creating an employee")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveEmployee(@Valid @RequestBody CreateEmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeDocService.saveEmployee(employeeDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get employee by id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Employee id")
        })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDoc> getEmployeeById(@PathVariable String id) {
        return new ResponseEntity<>(employeeDocService.getEmployeeById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Update employee by id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Employee id")
        })
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateEmployee(@PathVariable String id, @Valid @RequestBody EditEmployeeDto editEmployeeDto) {
        employeeDocService.updateEmployee(id, editEmployeeDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete employee by id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Employee id")
        })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable String id) {
        employeeDocService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
