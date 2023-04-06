package com.example.employeedata.helpers;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import com.example.employeedata.enums.*;
import com.example.employeedata.exception.CustomValidationException;

public final class CustomPropValidators {
    public static final int MIN_EMPLOYEE_AGE = 18;
    public static final int MAX_EMPLOYEE_AGE = 80;
    public static final int MAX_PROJECT_TEAM_SIZE = 100;

    public static void validateBirthDate(LocalDate birthDate, String errResource) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new CustomValidationException(
                    errResource, "birth date", birthDate,
                    String.format("%s birth date cannot be in the future", errResource));
        }

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

    public static void validateZoneId(ZonedDateTime zonedDateTime) {
        if (zonedDateTime.getZone() == null) {
            throw new CustomValidationException("zoneId/zoneOffset", "cannot be empty");
        } else {
            DateTimeHelpers.validateZoneId(zonedDateTime.getZone().getId());
        }
    }

    public static boolean areAllFieldsEmpty(String[] fields) {
        int count;

        count = (int) Stream.of(fields).filter(field -> field == null || field.isBlank()).count();

        return count == fields.length;
    }



}