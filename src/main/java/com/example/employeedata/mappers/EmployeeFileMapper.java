package com.example.employeedata.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.employeedata.dto.EmployeeFileDto;
import com.example.employeedata.enums.*;

public final class EmployeeFileMapper {
    public static EmployeeFileDto mapToEmployeeFileDto(Object[] employee) {
        EmployeeFileDto fileDto = new EmployeeFileDto();

        fileDto.setFirstName(employee[0].toString());
        fileDto.setLastName(employee[1].toString());
        fileDto.setBirthDate(employee[2].toString());
        fileDto.setDevLanguage(DevLanguage.values()[Integer.parseInt(employee[3].toString())].label);
        fileDto.setRole(Role.values()[Integer.parseInt(employee[4].toString())].label);
        fileDto.setProjectIds(employee[5].toString());

        return fileDto;
    }
    
    public static EmployeeFileDto mapToEmployeeFileDto(EmployeeFileDto employee) {
        EmployeeFileDto fileDto = new EmployeeFileDto();

        fileDto.setFirstName(employee.getFirstName());
        fileDto.setLastName(employee.getLastName());
        fileDto.setBirthDate(employee.getBirthDate());
        fileDto.setDevLanguage(DevLanguage.values()[Integer.parseInt(employee.getDevLanguage())].label);
        fileDto.setRole(Role.values()[Integer.parseInt(employee.getRole())].label);
        fileDto.setProjectIds(employee.getProjectIds() == null ? "" : employee.getProjectIds());

        return fileDto;
    }
    
    public static List<EmployeeFileDto> mapToListEmployeeFileDto(List<EmployeeFileDto> employees) {
        if (!employees.isEmpty()) {
            return employees.stream().map(EmployeeFileMapper::mapToEmployeeFileDto).collect(Collectors.toList());
        }

        return new ArrayList<EmployeeFileDto>();
    }
}
