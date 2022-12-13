package com.example.employeedata.service;

import java.util.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Employee;

public interface EmployeeService {
    Employee save(EmployeeDto employeeDto);
    List<Employee> getAll();
    Employee getById(long id);
    List<Employee> getByProjectId(long id);
    Employee update(long id, EditEmployeeDto EditEmployeeDto);
    void delete(long id);
}
