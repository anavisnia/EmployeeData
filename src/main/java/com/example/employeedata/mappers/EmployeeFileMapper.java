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
        fileDto.setRole(Role.values()[Integer.parseInt(employee[3].toString())].label);
        fileDto.setDevLanguage(DevLanguage.values()[Integer.parseInt(employee[4].toString())].label);
        fileDto.setProjectIds(employee[5].toString());

        return fileDto;
    }
}
