package com.example.employeedata.mappers;

import java.util.*;

import com.example.employeedata.document.*;
import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Employee;
import com.example.employeedata.helpers.CustomPropValidators;
import com.example.employeedata.helpers.HelperFunctions;

public final class EmployeeDocMapper {
    public static String errResource = "Employee Doc";

    public static EmployeeDoc mapToEmployeeDoc(Employee employee) {
        EmployeeDoc employeeDoc = new EmployeeDoc();
        
        employeeDoc.setId(employee.getId().toString());
        employeeDoc.setFirstName(employee.getFirstName());
        employeeDoc.setLastName(employee.getLastName());
        employeeDoc.setBirthDate(employee.getBirthDate());
        employeeDoc.setDevLanguage(employee.getDevLanguage().label);
        employeeDoc.setRole(employee.getRole().label);
        employeeDoc.setProjects(new ArrayList<>());
        employeeDoc.setModificationDate(employee.getModificationDate());

        return employeeDoc;
    }

    public static EmployeeDoc mapToEmployeeDoc(Employee employee, List<ProjectDoc> projects) {
        EmployeeDoc employeeDoc = new EmployeeDoc();
        
        employeeDoc.setId(employee.getId().toString());
        employeeDoc.setFirstName(employee.getFirstName());
        employeeDoc.setLastName(employee.getLastName());
        employeeDoc.setBirthDate(employee.getBirthDate());
        employeeDoc.setDevLanguage(employee.getDevLanguage().label);
        employeeDoc.setRole(employee.getRole().label);
        employeeDoc.setProjects(projects);
        employeeDoc.setModificationDate(employee.getModificationDate());

        return employeeDoc;
    }

    public static EmployeeDoc mapToEmployeeDoc(CreateEmployeeDto createEmployeeDto) {
        EmployeeDoc employeeDoc = new EmployeeDoc();

        employeeDoc.setId(HelperFunctions.generateId());
        employeeDoc.setFirstName(createEmployeeDto.getFirstName());
        employeeDoc.setLastName(createEmployeeDto.getLastName());
        employeeDoc.setDevLanguage(CustomPropValidators.validateDevLang(createEmployeeDto.getDevLanguage(), errResource).label);
        employeeDoc.setRole(CustomPropValidators.validateRole(createEmployeeDto.getRole(), errResource).label);
        employeeDoc.setProjects(new ArrayList<>());
        employeeDoc.setModificationDate(new Date());

        return employeeDoc;
    }

    public static EmployeeDoc mapToEmployeeDoc(CreateEmployeeDto createEmployeeDto, List<ProjectDoc> projects) {
        EmployeeDoc employeeDoc = new EmployeeDoc();

        employeeDoc.setId(HelperFunctions.generateId());
        employeeDoc.setFirstName(createEmployeeDto.getFirstName());
        employeeDoc.setLastName(createEmployeeDto.getLastName());
        CustomPropValidators.validateBirthDate(createEmployeeDto.getBirthDate(), errResource);
        employeeDoc.setBirthDate(createEmployeeDto.getBirthDate());
        employeeDoc.setDevLanguage(CustomPropValidators.validateDevLang(createEmployeeDto.getDevLanguage(), errResource).label);
        employeeDoc.setRole(CustomPropValidators.validateRole(createEmployeeDto.getRole(), errResource).label);
        employeeDoc.setProjects(projects);
        employeeDoc.setModificationDate(new Date());

        return employeeDoc;
    }

    public static EmployeeDoc mapToEmployeeDoc(EmployeeDoc existingEmployee, EditEmployeeDto employeeDto, List<ProjectDoc> projects) {
        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource).label);
        existingEmployee.setRole(CustomPropValidators.validateRole(employeeDto.getRole(), errResource).label);
        existingEmployee.setProjects(projects);
        existingEmployee.setModificationDate(new Date());

        return existingEmployee;
    }
}
