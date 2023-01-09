package com.example.employeedata.controller;

import java.util.*;

import javax.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.service.EmployeeService;

import io.swagger.annotations.*;

@Api(tags = "Service to manage employees")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ApiOperation(value = "Creating an employee")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveEmployee(@Valid @RequestBody CreateEmployeeDto employeeDto) {
        return new ResponseEntity<ResponseDto>(employeeService.saveEmployee(employeeDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Returns a lst of all employees")
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getAllEmployees(), HttpStatus.OK);
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
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<EmployeeDto>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get employee by project id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Project id")
        })
    @GetMapping("/project/{id}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByProjectId(@PathVariable Long id) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getEmployeesByProjectId(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get employees by development language id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "devLanguage",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Development language id")
        })
    @GetMapping("/devLang/{devLanguage}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDevLanguage(@PathVariable Integer devLanguage) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getEmployeesByDevLanguage(devLanguage), HttpStatus.OK);
    }

    @ApiOperation(value = "Get employees by role id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "role",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Role id")
        })
    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByRole(@PathVariable Integer role) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getEmployeesByRole(role), HttpStatus.OK);
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
    public ResponseEntity<HttpStatus> updateEmployee(@PathVariable Long id, @Valid @RequestBody EditEmployeeDto editEmployeeDto) {
        employeeService.updateEmployee(id, editEmployeeDto);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
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
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}
