package com.example.employeedata.mappers;

import java.time.LocalDate;
import java.util.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.helpers.CustomPropValidators;

public final class EmployeeMapper {
    public static String errResource = "Employee";

    public static Employee mapToEmployee(CreateEmployeeDto employeeDto) {
        Employee employee = new Employee();
        
        employee.setFirstName(CustomPropValidators.normalizeStr(employeeDto.getFirstName()));
        employee.setLastName(CustomPropValidators.normalizeStr(employeeDto.getLastName()));
        CustomPropValidators.validateBirthDate(employeeDto.getBirthDate(), errResource);
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setRole(CustomPropValidators.validateRole(employeeDto.getRole(), errResource));
        employee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource));
        employee.setModificationDate(new Date());

        return employee;
    }

    public static Employee mapToEmployee(CreateEmployeeDto employeeDto, List<Project> projects) {
        Employee employee = new Employee();
        
        employee.setFirstName(CustomPropValidators.normalizeStr(employeeDto.getFirstName()));
        employee.setLastName(CustomPropValidators.normalizeStr(employeeDto.getLastName()));
        CustomPropValidators.validateBirthDate(employeeDto.getBirthDate(), errResource);
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setRole(CustomPropValidators.validateRole(employeeDto.getRole(), errResource));
        employee.setDevLanguage(CustomPropValidators.validateDevLang(employeeDto.getDevLanguage(), errResource));
        employee.setProjects(new HashSet<>(projects));
        employee.setModificationDate(new Date());

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
        existingEmployee.setModificationDate(new Date());

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
        existingEmployee.setModificationDate(new Date());
        
        return existingEmployee;
    }

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();

        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setBirthDate(employee.getBirthDate());
        dto.setDevLanguage(employee.getDevLanguage().label);
        dto.setRole(employee.getRole().label);
        dto.setProjectIds(getProjectIds(employee.getProjects()));

        return dto;
    }

    public static List<EmployeeDto> mapToListEmployeesDto(List<Employee> employees) {
        List<EmployeeDto> dtos = new ArrayList<EmployeeDto>();

        if (!employees.isEmpty()) {
            // for (Employee employee : employees) {
            //     dtos.add(mapToEmployeeDto(employee));
            // }

            employees.stream().forEach(e -> dtos.add(mapToEmployeeDto(e)));
        }

        return dtos;
    }

    public static Employee mapToEmployee(String[] employeeFieldsFromFile, List<Project> projects) {
        Employee employee = new Employee();
        
        employee.setFirstName(CustomPropValidators.normalizeStr(employeeFieldsFromFile[0]));
        employee.setLastName(CustomPropValidators.normalizeStr(employeeFieldsFromFile[1]));
        LocalDate employeeDateOfBirth = LocalDate.parse(employeeFieldsFromFile[2]);
        CustomPropValidators.validateBirthDate(employeeDateOfBirth, errResource);
        employee.setBirthDate(employeeDateOfBirth);
        employee.setRole(CustomPropValidators.validateRole(Integer.parseInt(employeeFieldsFromFile[3].replace(".0", "")), errResource));
        employee.setDevLanguage(CustomPropValidators.validateDevLang(Integer.parseInt(employeeFieldsFromFile[4].replace(".0", "")), errResource));
        employee.setProjects(new HashSet<>(projects));
        employee.setModificationDate(new Date());

        return employee;
    }

    private static List<Long> getProjectIds(Set<Project> projects) {
        List<Long> projectIds = new ArrayList<>();

        if (!projects.isEmpty()) {
            // for (Project project : projects) {
            //     projectIds.add(project.getId());
            // }

            projects.forEach(p -> projectIds.add(p.getId()));
        }

        return projectIds;
    }
}
