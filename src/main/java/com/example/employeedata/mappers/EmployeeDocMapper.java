package com.example.employeedata.mappers;

import java.util.*;

import com.example.employeedata.document.*;
import com.example.employeedata.dto.*;
import com.example.employeedata.helpers.*;

public final class EmployeeDocMapper {
    public static String errResource = "Employee Doc";

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
