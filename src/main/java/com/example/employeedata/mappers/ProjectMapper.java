package com.example.employeedata.mappers;

import java.util.*;
import java.util.stream.Collectors;
import java.time.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Project;
import com.example.employeedata.helpers.CustomPropValidators;

public class ProjectMapper {
    public static String errResource = "Project";

    public static Project mapToProject(CreateProjectDto projectDto) {
        Project project = new Project();

        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setCustomer(projectDto.getCustomer());
        CustomPropValidators.validateTeamSize(projectDto.getTeamSize(), errResource);
        project.setTeamSize(projectDto.getTeamSize());
        project.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        project.setTerminationDate(projectDto.getTerminationDate());
        project.setCompletionDate(projectDto.getCompletionDate());
        project.setModificationDate(new Date());

        return project;
    }

    public static void mapToProject(Project existingProject, EditProjectDto projectDto) {
        existingProject.setTitle(projectDto.getTitle());
        existingProject.setDescription(projectDto.getDescription());
        existingProject.setCustomer(projectDto.getCustomer());
        CustomPropValidators.validateTeamSize(projectDto.getTeamSize(), errResource);
        existingProject.setTeamSize(projectDto.getTeamSize());
        existingProject.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        existingProject.setTerminationDate(projectDto.getTerminationDate());
        existingProject.setCompletionDate(projectDto.getCompletionDate());
        existingProject.setModificationDate(new Date());
    }

    public static ProjectDto mapToProjectDto(Project project) {
        ProjectDto dto = new ProjectDto();

        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setCustomer(project.getCustomer());
        dto.setTeamSize(project.getTeamSize());
        dto.setTerminationDate(project.getTerminationDate());
        dto.setCompletionDate(project.getCompletionDate());
        dto.setDevLanguage(project.getDevLanguage().label);

        return dto;
    }

    public static Project mapToProject(String[] projectFieldsFromFile) {
        Project project = new Project();
        
        project.setTitle(CustomPropValidators.normalizeStr(projectFieldsFromFile[0]));
        project.setDescription(CustomPropValidators.normalizeStr(projectFieldsFromFile[1]));
        project.setCustomer(CustomPropValidators.normalizeStr(projectFieldsFromFile[2]));
        project.setTeamSize(CustomPropValidators.validateTeamSize(Integer.parseInt(projectFieldsFromFile[3].replace(".0", "")), errResource));
        project.setDevLanguage(CustomPropValidators.validateDevLang(Integer.parseInt(projectFieldsFromFile[4].replace(".0", "")), errResource));
        LocalDate terminationDate = LocalDate.parse(projectFieldsFromFile[5]);
        CustomPropValidators.validateTerminationDate(terminationDate, errResource);
        project.setTerminationDate(terminationDate);
        LocalDateTime completionDate = LocalDateTime.parse(projectFieldsFromFile[6]);
        project.setCompletionDate(completionDate);
        project.setModificationDate(new Date());

        return project;
    }

}
