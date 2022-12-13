package com.example.employeedata.controller;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Employee;
import com.example.employeedata.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<Employee>(employeeService.save(employeeDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> allEmployees() {
        return new ResponseEntity<List<Employee>>(employeeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<Employee>(employeeService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<Employee>> getEmployeesByProjectId(@PathVariable(name = "id") long id) {
        return new ResponseEntity<List<Employee>>(employeeService.getByProjectId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(name = "id") long id, @RequestBody EditEmployeeDto editEmployeeDto) {
        return new ResponseEntity<Employee>(employeeService.update(id, editEmployeeDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable(name = "id") long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
