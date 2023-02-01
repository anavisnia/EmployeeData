package com.example.employeedata.controller;

import java.util.*;

import javax.validation.Valid;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;
import com.example.employeedata.service.EmployeeService;
import com.example.employeedata.service.EmployeeDocService;

import io.swagger.annotations.*;

@Api(tags = "Service to manage employees")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController<E> {
    private final EmployeeService employeeService;
    private final EmployeeDocService employeeDocService;

    public EmployeeController(
        EmployeeService employeeService,
        EmployeeDocService employeeDocService) {
        this.employeeService = employeeService;
        this.employeeDocService = employeeDocService;
    }

    @ApiOperation(value = "Creating an employee")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveEmployee(@Valid @RequestBody CreateEmployeeDto employeeDto) {
        return new ResponseEntity<ResponseDto>(employeeService.saveEmployee(employeeDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Creating employees from exel file")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "file",
                required = true,
                dataType = "file",
                value = "Exel file of .xls or .xlsx format with employee data")
        })
    //for now works only with postman
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDto> saveEmployeesFromFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<ResponseDto>(employeeService.saveEmployeesFromExelFile(file), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Returns a lst of all employees")
    @GetMapping
    public ResponseEntity<List<E>> getAllEmployees() {
        List<E> res = null;

        try {
            res = (List<E>) employeeDocService.getAllEmployees();
            if (res == null) {
                res = (List<E>) employeeService.getAllEmployees();
            }
        } catch (DataAccessResourceFailureException e) {
            res = (List<E>) employeeService.getAllEmployees();
        }

        return new ResponseEntity<List<E>>(res, HttpStatus.OK);
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
    public ResponseEntity<E> getEmployeeById(@PathVariable String id) {
        E res = null;

        try {
            res = (E) employeeDocService.getEmployeeById(id);

            if (res == null) {
                res = (E) employeeService.getEmployeeById(id.toString());
            }
        } catch (DataAccessResourceFailureException e) {
            res = (E) employeeService.getEmployeeById(id.toString());
        }

        return new ResponseEntity<E>(res, HttpStatus.OK);
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
