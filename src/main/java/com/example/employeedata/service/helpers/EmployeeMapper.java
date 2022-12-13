package com.example.employeedata.service.helpers;

import java.util.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;

public class EmployeeMapper {
    public static String errResource = "Employee";

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        
        employee.setFirstName(CustomPropValidators.normalizeStr(employeeDto.getFirstName()));
        employee.setLastName(CustomPropValidators.normalizeStr(employeeDto.getLastName()));
        CustomPropValidators.validateBirthDate(employeeDto.getBirthDate(), errResource);
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setRole(CustomPropValidators.validateRole(employeeDto.getRole(), errResource));
        employee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource));

        return employee;
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto, List<Project> projects) {
        Employee employee = new Employee();
        
        employee.setFirstName(CustomPropValidators.normalizeStr(employeeDto.getFirstName()));
        employee.setLastName(CustomPropValidators.normalizeStr(employeeDto.getLastName()));
        CustomPropValidators.validateBirthDate(employeeDto.getBirthDate(), errResource);
        employee.setBirthDate(employeeDto.getBirthDate());
        CustomPropValidators.validateRole(employeeDto.getRole(), errResource);
        employee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource));
        employee.setProjects(new HashSet<>(projects));

        return employee;
    }

    public static Employee mapToEmployee(Employee existingEmployee, EditEmployeeDto employeeDto) {
        existingEmployee.setId(existingEmployee.getId());
        existingEmployee.setFirstName(CustomPropValidators.normalizeStr(employeeDto.getFirstName()));
        existingEmployee.setLastName(CustomPropValidators.normalizeStr(employeeDto.getLastName()));
        CustomPropValidators.validateBirthDate(existingEmployee.getBirthDate(), errResource);
        existingEmployee.setBirthDate(existingEmployee.getBirthDate());
        existingEmployee.setRole(CustomPropValidators.validateRole(employeeDto.getRole(), errResource));
        existingEmployee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource));
        existingEmployee.setProjects(new HashSet<>());

        return existingEmployee;
    }

    public static Employee mapToEmployee(Employee existingEmployee, EditEmployeeDto employeeDto, List<Project> projects) {
        existingEmployee.setId(existingEmployee.getId());
        existingEmployee.setFirstName(CustomPropValidators.normalizeStr(employeeDto.getFirstName()));
        existingEmployee.setLastName(CustomPropValidators.normalizeStr(employeeDto.getLastName()));
        CustomPropValidators.validateBirthDate(existingEmployee.getBirthDate(), errResource);
        existingEmployee.setBirthDate(existingEmployee.getBirthDate());
        existingEmployee.setRole(CustomPropValidators.validateRole(employeeDto.getRole(), errResource));
        existingEmployee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource));
        existingEmployee.setProjects(new HashSet<>(projects));
        
        return existingEmployee;
    }
}
