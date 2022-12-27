package com.example.employeedata.controller;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveEmployee(@RequestBody CreateEmployeeDto employeeDto) {
        return new ResponseEntity<ResponseDto>(employeeService.saveEmployee(employeeDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<EmployeeDto>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByProjectId(@PathVariable Long id) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getEmployeesByProjectId(id), HttpStatus.OK);
    }

    @GetMapping("/devLang/{devLanguage}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDevLanguage(@PathVariable Integer devLanguage) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getEmployeesByDevLanguage(devLanguage), HttpStatus.OK);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByRole(@PathVariable Integer role) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getEmployeesByRole(role), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateEmployee(@PathVariable Long id, @RequestBody EditEmployeeDto editEmployeeDto) {
        employeeService.updateEmployee(id, editEmployeeDto);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}
