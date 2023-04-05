package com.example.employeedata.helpers;

import com.example.employeedata.dto.CreateEmployeeDto;
import com.example.employeedata.dto.EditEmployeeDto;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.enums.Role;
import com.example.employeedata.exception.CustomValidationException;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class EmployeeValidator {
    private static final String RES_NAME = "Employee";

    public static void validateCreateDto(CreateEmployeeDto dto) {
        dto.setFirstName(HelperFunctions.normalizeStr(dto.getFirstName()));
        dto.setLastName(HelperFunctions.normalizeStr(dto.getLastName()));
        CustomPropValidators.validateBirthDate(dto.getBirthDate(), RES_NAME);
        CustomPropValidators.validateRole(dto.getRole(), RES_NAME);
        CustomPropValidators.validateDevLang(dto.getDevLanguage(), RES_NAME);
    }

    public static void validateEditDto(EditEmployeeDto dto) {
        dto.setFirstName(HelperFunctions.normalizeStr(dto.getFirstName()));
        dto.setLastName(HelperFunctions.normalizeStr(dto.getLastName()));
        CustomPropValidators.validateRole(dto.getRole(), RES_NAME);
        CustomPropValidators.validateDevLang(dto.getDevLanguage(), RES_NAME);
    }

    public static void validateFile(String[] employeeData) {
        employeeData[0] = HelperFunctions.normalizeStr(employeeData[0]);
        employeeData[1] = HelperFunctions.normalizeStr(employeeData[1]);
    }

    public static boolean isValidEmployeeFile(String[] employee) {
        try {
            if (Strings.isBlank(employee[0]) ||
                    !Pattern.matches(Constants.REGEX_NAME, employee[0])
            ) {
                return false;
            } else if (Strings.isBlank(employee[1]) ||
                    !Pattern.matches(Constants.REGEX_NAME, employee[1])
            ) {
                return false;
            } else if (Strings.isBlank(employee[2]) ||
                    LocalDate.parse(employee[2]).isAfter(LocalDate.now())
            ) {
                return false;
            } else if (Strings.isBlank(employee[3]) ||
                    Long.parseLong(employee[3].replace(".0", "")) > (Role.size() - 1) ||
                    Long.parseLong(employee[3].replace(".0", "")) < 0
            ) {
                return false;
            } else if (Strings.isBlank(employee[4]) ||
                    Long.parseLong(employee[4].replace(".0", "")) > (DevLanguage.size() - 1) ||
                    Long.parseLong(employee[4].replace(".0", "")) < 0
            ) {
                return false;
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            return false;
        }

        try {
            CustomPropValidators.validateBirthDate(LocalDate.parse(employee[2]), RES_NAME);
        } catch (CustomValidationException e) {
            return false;
        }

        return true;
    }
}
