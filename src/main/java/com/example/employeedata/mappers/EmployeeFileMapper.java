package com.example.employeedata.mappers;

import com.example.employeedata.dto.EmployeeFileDto;
import com.example.employeedata.enums.*;
import com.example.employeedata.helpers.*;

public final class EmployeeFileMapper {
    public static EmployeeFileDto mapToEmployeeFileDto(Object[] employee) {
        EmployeeFileDto fileDto = new EmployeeFileDto();

        fileDto.setFirstName(HelperFunctions.normalizeStr(employee[0].toString()));
        fileDto.setLastName(HelperFunctions.normalizeStr(employee[1].toString()));
        fileDto.setBirthDate(employee[2].toString());
        fileDto.setRole(Role.values()[Integer.parseInt(employee[3].toString())].label);
        fileDto.setDevLanguage(DevLanguage.values()[Integer.parseInt(employee[4].toString())].label);
        fileDto.setProjectIds(employee[5].toString());

        return fileDto;
    }
}
