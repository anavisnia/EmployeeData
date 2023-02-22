package com.example.employeedata.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;

import com.example.employeedata.enums.*;
import com.example.employeedata.exception.CustomValidationException;

public final class CustomPropValidators {
    public static final int MIN_EMPLOYEE_AGE = 18;
    public static final int MAX_EMPLOYEE_AGE = 80;
    public static final int MAX_PROJECT_TEAM_SIZE = 100;

    public static void validateBirthDate(LocalDate birthDate, String errResource) {
        if (LocalDate.now().getYear() - birthDate.getYear() < MIN_EMPLOYEE_AGE) {
            throw new CustomValidationException(
                errResource, "birth date", birthDate,
                String.format("%s cannot be younger than %d years old", errResource, MIN_EMPLOYEE_AGE));
        } else if(LocalDate.now().getYear() - birthDate.getYear() > MAX_EMPLOYEE_AGE) {
            throw new CustomValidationException(
                errResource, "birth date", birthDate,
                String.format("%s cannot be older than %d years old", errResource, MAX_EMPLOYEE_AGE));
        }
    }

    public static Role validateRole(int role, String errResource) {
        Role[] roleValues = Role.values();
        if (role > (roleValues.length - 1) || role < 0) {
            throw new CustomValidationException(
                errResource, "role", role,
                errResource + "'s role cannot be out of scope");
        }

        return roleValues[role];
    }

    public static DevLanguage validateDevLang(int devLang, String errResource) {
        DevLanguage[] devLangValues = DevLanguage.values();
        if (devLang > (devLangValues.length - 1) || devLang < 0) {
            throw new CustomValidationException(
                errResource, "developer language", devLang,
                errResource + "'s development language cannot be out of scope");
        }

        return devLangValues[devLang];
    }

    public static void validateFileType(String type, String errResource) {
        List<String> types = Arrays.asList(FileTypes.labels());

        if (types.contains(type)) {
            return;
        }

        throw new CustomValidationException(errResource, type + " is not allowed");
    }

    public static Integer validateTeamSize(int teamSize, String errResource) {
        if(teamSize < 0) {
            throw new CustomValidationException(
                errResource, "team size", teamSize,
                errResource + "'s team size cannot be out of scope");
        }

        return teamSize;
    }

    public static boolean isValidEmployeeFile(String[] employee) {
        try {
            if (employee[0] == null ||
                    employee[0] != null && employee[0].isBlank() ||
                    employee[0] != null && !employee[0].isBlank() && !Pattern.matches(Constants.REGEX_NAME, employee[0])
                ) {
                return false;
            } else if (employee[1] == null ||
                    (employee[1] != null && employee[1].isBlank()) ||
                    (employee[1] != null && !employee[1].isBlank() && !Pattern.matches(Constants.REGEX_NAME, employee[1]))
                ) {
                return false;
            } else if (employee[2] == null ||
                    (employee[2] != null && employee[2].isBlank()) ||
                    (employee[2] != null && !employee[2].isBlank() && LocalDate.parse(employee[3]).isBefore(LocalDate.now()))
                ) {
                return false;
            } else if (employee[3] == null ||
                    (employee[3] != null && employee[3].isBlank()) ||
                    (employee[3] != null && !employee[3].isBlank() && Long.parseLong(employee[3].replace(".0", "")) > (Role.size() - 1)) ||
                    (employee[3] != null && !employee[3].isBlank() && Long.parseLong(employee[3].replace(".0", "")) < 0)
                ) {
                return false;
            } else if (employee[4] == null ||
                    (employee[4] != null && employee[4].isBlank()) ||
                    (employee[4] != null && !employee[4].isBlank() && Long.parseLong(employee[4].replace(".0", "")) > (DevLanguage.size() - 1)) ||
                    (employee[4] != null && !employee[4].isBlank() && Long.parseLong(employee[4].replace(".0", "")) < 0)
                ) {
                return false;
            }
        } catch(NumberFormatException | DateTimeParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidProjectFile(String[] project) {
        try {
            if (project[0] == null ||
                    (project[0] != null && project[0].isBlank()) ||
                    (project[0] != null && !project[0].isBlank() && !Pattern.matches(Constants.REGEX_TEXT_WITHOUT_SYMBOLS, project[0]))
                ) {
                return false;
            } else if (project[1] == null ||
                    (project[1] != null && project[1].isBlank()) ||
                    (project[1] != null && !project[1].isBlank() && !Pattern.matches(Constants.REGEX_TEXT_WITHOUT_SYMBOLS, project[1]))
                ) {
                return false;
            } else if (project[2] == null ||
                    (project[2] != null && project[2].isBlank()) ||
                    (project[2] != null && !project[2].isBlank() && !Pattern.matches(Constants.REGEX_TEXT_WITH_SYMBOLS, project[2]))
                ) {
                return false;
            } else if (project[3] == null || 
                    (project[3] != null && project[3].isBlank()) ||
                    (!project[3].isBlank() && Long.parseLong(project[3].replace(".0", "")) < 0) ||
                    (!project[3].isBlank() && Long.parseLong(project[3].replace(".0", "")) > MAX_PROJECT_TEAM_SIZE)
                ) {
                return false;
            } else if (project[4] == null ||
                    (project[4] != null && project[4].isBlank()) ||
                    (project[4] != null && !project[4].isBlank() && Long.parseLong(project[4].replace(".0", "")) > (DevLanguage.size() - 1)) ||
                    (project[4] != null && !project[4].isBlank() && Long.parseLong(project[4].replace(".0", "")) < 0)
                ) {
                return false;
            } else if (project[5] == null ||
                    (project[5] != null && project[5].isBlank()) ||
                    (project[5] != null && !project[5].isBlank() && LocalDate.parse(project[5]).isBefore(LocalDate.now()))
                ) {
                return false;
            }
        } catch(NumberFormatException | DateTimeParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isMaxReachedForEmptyFields(String[] fields, Integer maxAllowedEmptyFields) {
        Integer count = 0;
        
        count = (int) Stream.of(fields).filter(field -> field == null || (field != null && field.isBlank())).count();

        if (count > maxAllowedEmptyFields) {
            return true;
        }

        return false;
    }

    public static boolean areAllFieldsEmpty(String[] fields) {
        Integer count = 0;

        count = (int) Stream.of(fields).filter(field -> field == null || (field != null && field.isBlank())).count();

        if (count == fields.length) {
            return true;
        }

        return false;
    }

    public static String normalizeStr(String str) {
        if (str.isBlank()) {
            throw new CustomValidationException("text", "Text" + " cannot be blank");
        }

        String normalized = str.trim().toLowerCase();

        char[] charracters = normalized.toCharArray();

        StringBuilder finalStr = new StringBuilder();

        for (int i = 0; i < charracters.length; i++) {
            if (i == 0) {
                Character c = charracters[i];
                finalStr.append(Character.toUpperCase(c));
                continue;
            }
            finalStr.append(charracters[i]);
        }

        return finalStr.toString();
    }

    public static void isProperFileType(String fileName) {
        String fileExtension = "";
        String errorResource = "File type";

        if (fileName.isBlank()) {
            throw new CustomValidationException(errorResource, fileExtension + " is not allowed");
        }

        fileExtension = FileHelperFunctions.getExtensionFromFileName(fileName);
        validateFileType(fileExtension, errorResource);
    }

    public static void validateTerminationDate(LocalDate terminationDate, String errResource) {
        if (!terminationDate.isAfter(LocalDate.now())) {
            throw new CustomValidationException(
                errResource, "termiantion date", terminationDate,
                String.format("%s cannot be before todays date", errResource));
        }
    }

    public static Integer checkPageSzie(Integer pageSize) {
        if (pageSize == null || (pageSize != null && pageSize <= 0)) {
           return 10;
        } else if (pageSize > 500) {
            return 500;
        }

        return pageSize;
    }

    public static String checkFiledSorting(String[] entityFields, Integer sortBy) {
        if(sortBy == null ||
            sortBy != null && sortBy < 0 ||
            sortBy != null && sortBy > entityFields.length - 1) {
            return "id"; //default
        }

        return entityFields[sortBy];
    }

    public static Pageable returnPageableWithSorting(Integer pageNumber, Integer pageSize, String query, String isAsc) {
        Boolean order = Boolean.parseBoolean(isAsc);

        if(query == null || query != null && query.isBlank()) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(order == true ? Direction.ASC : Direction.DESC));
        } else if(isAsc == null || (isAsc != null && isAsc.isBlank())) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(query));
        } else if (order == true) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(query).ascending());
        } else {
            return PageRequest.of(pageNumber, pageSize, Sort.by(query).descending());
        }
    }
}