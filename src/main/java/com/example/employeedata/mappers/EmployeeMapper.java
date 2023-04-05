package com.example.employeedata.mappers;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.enums.*;
import org.apache.logging.log4j.util.Strings;

public final class EmployeeMapper {
    public static Employee mapToEmployee(CreateEmployeeDto employeeDto) {
        Employee employee = new Employee();
        
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setRole(Role.values()[employeeDto.getRole()]);
        employee.setDevLanguage(DevLanguage.values()[employeeDto.getDevLanguage()]);
        employee.setModificationDate(new Date());

        return employee;
    }

    public static Employee mapToEmployee(CreateEmployeeDto employeeDto, Collection<Project> projects) {
        Employee employee = new Employee();
        
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setRole(Role.values()[employeeDto.getRole()]);
        employee.setDevLanguage(DevLanguage.values()[employeeDto.getDevLanguage()]);
        employee.setProjects(new HashSet<>(projects));
        employee.setModificationDate(new Date());

        return employee;
    }

    public static void mapToEmployee(Employee existingEmployee, EditEmployeeDto employeeDto) {
        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setRole(Role.values()[employeeDto.getRole()]);
        existingEmployee.setDevLanguage(DevLanguage.values()[employeeDto.getDevLanguage()]);
        existingEmployee.setProjects(new HashSet<>());
        existingEmployee.setModificationDate(new Date());
    }

    public static void mapToEmployee(Employee existingEmployee, EditEmployeeDto employeeDto, Collection<Project> projects) {
        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setRole(Role.values()[employeeDto.getRole()]);
        existingEmployee.setDevLanguage(DevLanguage.values()[employeeDto.getDevLanguage()]);
        existingEmployee.setProjects(new HashSet<>(projects));
        existingEmployee.setModificationDate(new Date());
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

    public static Employee mapToEmployee(String[] employeeFieldsFromFile, Collection<Project> projects) {
        Employee employee = new Employee();
        
        employee.setFirstName(employeeFieldsFromFile[0]);
        employee.setLastName(employeeFieldsFromFile[1]);
        LocalDate employeeDateOfBirth = LocalDate.parse(employeeFieldsFromFile[2]);
        employee.setBirthDate(employeeDateOfBirth);
        employee.setRole(Role.values()[Integer.parseInt(employeeFieldsFromFile[3].replace(".0", ""))]);
        employee.setDevLanguage(DevLanguage.values()[Integer.parseInt(employeeFieldsFromFile[4].replace(".0", ""))]);
        employee.setProjects(new HashSet<>(projects));
        employee.setModificationDate(new Date());

        return employee;
    }

    private static List<Long> getProjectIds(Collection<Project> projects) {
        if (!projects.isEmpty()) {
            return projects.stream().map(Project::getId).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public static String[] mapToEmployeeStringArr(Employee employee) {
        String[] employeeArr = {
                Strings.isBlank(employee.getFirstName()) ? "" : employee.getFirstName(),
                Strings.isBlank(employee.getLastName()) ? "" : employee.getLastName(),
                Strings.isBlank(employee.getBirthDate().toString()) ? "" : employee.getBirthDate().toString(),
                Strings.isBlank(employee.getRole().label) ? "" : employee.getRole().label,
                Strings.isBlank(employee.getDevLanguage().label) ? "" : employee.getDevLanguage().label,
                employee.getProjects().isEmpty() ? "" : getProjectIds(employee.getProjects()).toString()
        };

        return employeeArr;
    }
}
