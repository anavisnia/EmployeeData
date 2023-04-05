package com.example.employeedata.helpers;

import com.example.employeedata.dto.CreateProjectDto;
import com.example.employeedata.dto.EditProjectDto;
import com.example.employeedata.enums.DevLanguage;
import org.apache.logging.log4j.util.Strings;

import java.time.DateTimeException;
import java.util.regex.Pattern;

public class ProjectValidator {
    private static final String RES_NAME = "Project";

    public static void validateCreateDto(CreateProjectDto dto) {
        CustomPropValidators.validateTeamSize(dto.getTeamSize(), RES_NAME);
        CustomPropValidators.validateDevLang(dto.getDevLanguage(), RES_NAME);
    }

    public static void validateEditDto(EditProjectDto dto) {
        CustomPropValidators.validateTeamSize(dto.getTeamSize(), RES_NAME);
        CustomPropValidators.validateDevLang(dto.getDevLanguage(), RES_NAME);
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
                            Long.parseLong(project[3].replace(".0", "")) > CustomPropValidators.MAX_PROJECT_TEAM_SIZE)
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
}
