package com.example.employeedata.controller;

import java.time.LocalDate;
import java.util.*;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;
import com.example.employeedata.service.EmployeeService;
import com.example.employeedata.service.EmployeeDocService;
import com.example.employeedata.helpers.Constants;
import com.example.employeedata.helpers.DateTimeHelpers;

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
        return new ResponseEntity<>(employeeService.saveEmployee(employeeDto), HttpStatus.CREATED);
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
        return new ResponseEntity<>(employeeService.saveEmployeesFromExelFile(file), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Returns a lst of all employees")
    @GetMapping
    public ResponseEntity<List<E>> getAllEmployees() {
        List<E> res;

        try {
            res = (List<E>) employeeDocService.getAllEmployees();
            if (res == null) {
                res = (List<E>) employeeService.getAllEmployees();
            }
        } catch (DataAccessResourceFailureException e) {
            res = (List<E>) employeeService.getAllEmployees();
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a list of all employees with paging information")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "searchQuery",
                dataType = "string",
                value = "Text by which database will retrieve similar data"),
            @ApiImplicitParam(
                name = "pageNumber",
                dataType = "string",
                value = "Page number"),
            @ApiImplicitParam(
                name = "pageSize",
                dataType = "string",
                value = "Size of entities per page"),
            @ApiImplicitParam(
                name = "sortBy",
                dataType = "integer",
                value = "Entity parameter by which entities will be sorted"),
            @ApiImplicitParam(
                name = "isAsc",
                dataType = "string",
                value = "Is sorting ascending")
        })
    @GetMapping("/page")
    public ResponseEntity<PaginatedResponseDto<EmployeeDto>> getAllEmployeesPage(
                @RequestParam(required = false) String searchQuery,
                @RequestParam(required = false) Integer pageNumber,
                @RequestParam(required = false) Integer pageSize,
                @RequestParam(required = false) Integer sortBy,
                @RequestParam(required = false) String isAsc) {
        return new ResponseEntity<>(employeeService.getAllEmployeesPage(searchQuery, pageNumber, pageSize, sortBy, isAsc), HttpStatus.OK);
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
        E res;

        try {
            res = (E) employeeDocService.getEmployeeById(id);

            if (res == null) {
                res = (E) employeeService.getEmployeeById(id);
            }
        } catch (DataAccessResourceFailureException e) {
            res = (E) employeeService.getEmployeeById(id);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
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
        return new ResponseEntity<>(employeeService.getEmployeesByProjectId(id), HttpStatus.OK);
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
        return new ResponseEntity<>(employeeService.getEmployeesByDevLanguage(devLanguage), HttpStatus.OK);
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
        return new ResponseEntity<>(employeeService.getEmployeesByRole(role), HttpStatus.OK);
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
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get a list of employees in an Exel file.")
    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadEmployeesInExelFile() {
        byte[] byteArr = employeeService.generateExelFile();
        
        if(byteArr.length == 0) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArr);
        LocalDate date = DateTimeHelpers.getLocalDateNow();
        String contentType = Constants.DOWNLOAD_OCTET_STREAM;
        String headerValue = Constants.ATTACHMENT_FILENAME + Constants.EMPLOYEE_FILE_NAME + date + ".xlsx";
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @ApiOperation(value = "Get a map of employees grouped by development language.")
    @GetMapping("/byDevLanguage")
    public ResponseEntity<Map<String, List<EmployeeDto>>> getEmployeesGroupedByDevLanguage() {
        return new ResponseEntity<>(employeeService.getEmployeesGroupedByDevLanguage(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a map of employees grouped by role.")
    @GetMapping("/byRole")
    public ResponseEntity<Map<String, List<EmployeeDto>>> getEmployeesGroupedByRole() {
        return new ResponseEntity<>(employeeService.getEmployeesGroupedByRole(), HttpStatus.OK);
    }
}
