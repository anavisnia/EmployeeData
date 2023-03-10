package com.example.employeedata.helpers;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;

import com.example.employeedata.enums.*;
import com.example.employeedata.exception.CustomValidationException;

public final class CustomPropValidators {
    public static final int MIN_EMPLOYEE_AGE = 18;
    public static final int MAX_EMPLOYEE_AGE = 80;
    public static final int MAX_PROJECT_TEAM_SIZE = 100;

    public static void validateBirthDate(LocalDate birthDate, String errResource) {
        long employeeAge = java.time.temporal.ChronoUnit.YEARS.between(birthDate, LocalDate.now());

        if (employeeAge < MIN_EMPLOYEE_AGE) {
            throw new CustomValidationException(
                    errResource, "birth date", birthDate,
                    String.format("%s cannot be younger than %d years old", errResource, MIN_EMPLOYEE_AGE));
        } else if (employeeAge > MAX_EMPLOYEE_AGE) {
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

    public static Integer validateTeamSize(int teamSize, String errResource) {
        if (teamSize < 0) {
            throw new CustomValidationException(
                errResource, "team size", teamSize,
                errResource + "'s team size cannot be out of scope");
        }

        return teamSize;
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
            validateBirthDate(LocalDate.parse(employee[2]), "Employee");
        } catch (CustomValidationException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidProjectFile(String[] project, String zoneId) {
        try {
            if (Strings.isBlank(project[0]) ||
                    !Pattern.matches(Constants.REGEX_TEXT_WITHOUT_SYMBOLS, project[0])
            ) {
                return false;
            } else if (Strings.isNotBlank(project[1]) &&
                    !Pattern.matches(Constants.REGEX_TEXT_WITH_SYMBOLS, project[1])
            ) {
                return false;
            } else if (Strings.isBlank(project[2]) ||
                    !Pattern.matches(Constants.REGEX_TEXT_WITH_SYMBOLS, project[2])
            ) {
                return false;
            } else if ((Strings.isNotBlank(project[3]) &&
                    Long.parseLong(project[3].replace(".0", "")) < 0)
                    ||
                    (Strings.isNotBlank(project[3]) &&
                    Long.parseLong(project[3].replace(".0", "")) > MAX_PROJECT_TEAM_SIZE)
            ) {
                return false;
            } else if (Strings.isBlank(project[4]) ||
                    Long.parseLong(project[4].replace(".0", "")) > (DevLanguage.size() - 1) ||
                    Long.parseLong(project[4].replace(".0", "")) < 0
            ) {
                return false;
            } else if (Strings.isBlank(project[5]) ||
                    DateTimeHelpers.getFormattedZDTFromString(project[5], zoneId)
                        .isBefore(DateTimeHelpers.getFormattedZDTFromLDTNow(zoneId))
            ) {
                return false;
            } else if (Strings.isNotBlank(project[6]) &&
                DateTimeHelpers.getFormattedZDTFromString(project[6], zoneId)
                    .isAfter(DateTimeHelpers.getFormattedZDTFromString(project[5], zoneId)) ||
                Strings.isNotBlank(project[6]) &&
                DateTimeHelpers.getFormattedZDTFromString(project[6], zoneId)
                    .isAfter(DateTimeHelpers.getFormattedZDTFromLDTNow(zoneId))
            ) {
                return false;
            }
        } catch (NumberFormatException | DateTimeException e) {
            return false;
        }

        return true;
    }

    public static boolean areAllFieldsEmpty(String[] fields) {
        int count;

        count = (int) Stream.of(fields).filter(field -> field == null || field.isBlank()).count();

        return count == fields.length;
    }

    public static String normalizeStr(String str) {
        if (str.isBlank()) {
            throw new CustomValidationException("text", "Text" + " cannot be blank");
        }

        String normalized = str.trim().toLowerCase();

        char[] characters = normalized.toCharArray();

        StringBuilder finalStr = new StringBuilder();

        for (int i = 0; i < characters.length; i++) {
            if (i == 0) {
                char c = characters[i];
                finalStr.append(Character.toUpperCase(c));
                continue;
            }
            finalStr.append(characters[i]);
        }

        return finalStr.toString();
    }

}