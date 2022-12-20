package com.example.employeedata.helpers;

import java.time.LocalDate;

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

    public static void validateTeamSize(int teamSize, String errResource) {
        if(teamSize < 0) {
            throw new CustomValidationException(
                errResource, "team size", teamSize,
                errResource + "'s team size cannot be out of scope");
        }
    }

    public static String normalizeStr(String str) {
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
}