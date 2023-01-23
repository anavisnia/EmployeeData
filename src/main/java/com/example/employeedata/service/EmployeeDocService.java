package com.example.employeedata.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employeedata.document.EmployeeDoc;
import com.example.employeedata.dto.*;

@Service
public interface EmployeeDocService {
    ResponseDto saveEmployee(CreateEmployeeDto employeeDto);
    List<EmployeeDoc> getAllEmployees();
    EmployeeDoc getEmployeeById(String employeeId);
    void updateEmployee(String employeeId, EditEmployeeDto EditEmployeeDto);
    void deleteEmployee(String employeeId);
}
