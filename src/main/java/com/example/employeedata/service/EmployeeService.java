package com.example.employeedata.service;

import java.util.*;

import com.example.employeedata.dto.*;

public interface EmployeeService {
    EmployeeDto saveEmployee(CreateEmployeeDto employeeDto);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeById(Long id);
    List<EmployeeDto> getEmployeeByProjectId(Long id);
    void updateEmployee(Long id, EditEmployeeDto EditEmployeeDto);
    void deleteEmployee(Long id);
}
