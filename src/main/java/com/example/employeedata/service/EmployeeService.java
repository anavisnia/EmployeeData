package com.example.employeedata.service;

import java.util.*;

import com.example.employeedata.dto.*;

public interface EmployeeService {
    ResponseDto saveEmployee(CreateEmployeeDto employeeDto);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeById(Long employeeId);
    List<EmployeeDto> getEmployeeByProjectId(Long projectId);
    void updateEmployee(Long employeeId, EditEmployeeDto EditEmployeeDto);
    void deleteEmployee(Long employeeId);
}
