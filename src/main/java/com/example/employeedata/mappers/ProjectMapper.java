package com.example.employeedata.mappers;

import java.util.*;
import java.time.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Project;
import com.example.employeedata.helpers.CustomPropValidators;
import com.example.employeedata.helpers.DateTimeHelpers;
import org.apache.logging.log4j.util.Strings;

public class ProjectMapper {
    public static final String errResource = "Project";

    public static Project mapToProject(CreateProjectDto projectDto, String zoneId) {
        Project project = new Project();

        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setCustomer(projectDto.getCustomer());
        CustomPropValidators.validateTeamSize(projectDto.getTeamSize(), errResource);
        project.setTeamSize(projectDto.getTeamSize());
        project.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        project.setTerminationDate(DateTimeHelpers.GetZDTFromLDT(projectDto.getTerminationDate(), zoneId));
        project.setCompletionDate(projectDto.getCompletionDate() == null ? null : DateTimeHelpers.GetZDTFromLDT(projectDto.getCompletionDate(), zoneId));
        project.setModificationDate(DateTimeHelpers.GetZDTFromLDTNow(zoneId));

        return project;
    }

    public static void mapToProject(Project existingProject, EditProjectDto projectDto, String zoneId) {
        existingProject.setTitle(projectDto.getTitle());
        existingProject.setDescription(projectDto.getDescription());
        existingProject.setCustomer(projectDto.getCustomer());
        CustomPropValidators.validateTeamSize(projectDto.getTeamSize(), errResource);
        existingProject.setTeamSize(projectDto.getTeamSize());
        existingProject.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        existingProject.setTerminationDate(DateTimeHelpers.GetZDTFromLDT(projectDto.getTerminationDate(), zoneId));
        existingProject.setCompletionDate(projectDto.getCompletionDate() == null ? null : DateTimeHelpers.GetZDTFromLDT(projectDto.getCompletionDate(), zoneId));
        existingProject.setModificationDate(DateTimeHelpers.GetZDTFromLDTNow(zoneId));
    }

    public static ProjectDto mapToProjectDto(Project project, String zoneId) {
        ProjectDto dto = new ProjectDto();

        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setCustomer(project.getCustomer());
        dto.setTeamSize(project.getTeamSize());
        dto.setTerminationDate(DateTimeHelpers.GetLDTFromZDT(project.getTerminationDate(), zoneId));
        dto.setCompletionDate(project.getCompletionDate() == null ? null : DateTimeHelpers.GetLDTFromZDT(project.getCompletionDate(), zoneId));
        dto.setDevLanguage(project.getDevLanguage().label);

        return dto;
    }

    public static Project mapToProject(String[] projectFieldsFromFile, String zoneId) {
        Project project = new Project();
        
        project.setTitle(CustomPropValidators.normalizeStr(projectFieldsFromFile[0]));
        project.setDescription(CustomPropValidators.normalizeStr(projectFieldsFromFile[1]));
        project.setCustomer(CustomPropValidators.normalizeStr(projectFieldsFromFile[2]));
        project.setTeamSize(CustomPropValidators.validateTeamSize(Integer.parseInt(projectFieldsFromFile[3].replace(".0", "")), errResource));
        project.setDevLanguage(CustomPropValidators.validateDevLang(Integer.parseInt(projectFieldsFromFile[4].replace(".0", "")), errResource));
        LocalDateTime terminationDate = LocalDateTime.parse(projectFieldsFromFile[5]);
        //CustomPropValidators.validateTerminationDate(terminationDate, errResource);
        project.setTerminationDate(DateTimeHelpers.GetZDTFromLDT(terminationDate, zoneId));
        String completionDateField = projectFieldsFromFile[6];
        if (Strings.isNotBlank(completionDateField)) {
            LocalDateTime completionDate = LocalDateTime.parse(projectFieldsFromFile[6]);
            project.setCompletionDate(DateTimeHelpers.GetZDTFromLDT(completionDate, zoneId));
        }
        project.setModificationDate(DateTimeHelpers.GetZDTFromLDTNow(zoneId));

        return project;
    }

}
