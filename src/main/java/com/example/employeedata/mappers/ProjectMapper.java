package com.example.employeedata.mappers;

import java.util.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Project;
import com.example.employeedata.helpers.CustomPropValidators;

public class ProjectMapper {
    public static String errResource = "Project";

    public static Project mapToProject(CreateProjectDto projectDto) {
        Project project = new Project();

        project.setTitle(projectDto.getTitle());
        project.setCustomer(projectDto.getCustomer());
        CustomPropValidators.validateTeamSize(projectDto.getTeamSize(), errResource);
        project.setTeamSize(projectDto.getTeamSize());
        project.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        project.setTerminationDate(projectDto.getTerminationDate());

        return project;
    }

    public static Project mapToProject(Project existingProject, EditProjectDto projectDto) {
        existingProject.setId(existingProject.getId());
        existingProject.setTitle(projectDto.getTitle());
        existingProject.setCustomer(projectDto.getCustomer());
        CustomPropValidators.validateTeamSize(projectDto.getTeamSize(), errResource);
        existingProject.setTeamSize(projectDto.getTeamSize());
        existingProject.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        existingProject.setTerminationDate(projectDto.getTerminationDate());
        
        return existingProject;
    }

    public static ProjectDto mapToProjectDto(Project project) {
        ProjectDto dto = new ProjectDto();

        dto.setTitle(project.getTitle());
        dto.setCustomer(project.getCustomer());
        dto.setTeamSize(project.getTeamSize());
        dto.setTerminationDate(project.getTerminationDate());
        dto.setDevLanguage(project.getDevLanguage().ordinal());

        return dto;
    }

    public static List<ProjectDto> mapToListProjectsDto(List<Project> projects) {
        List<ProjectDto> dtos = new ArrayList<ProjectDto>();

        if (!projects.isEmpty()) {
            for (Project project : projects) {
                dtos.add(mapToProjectDto(project));
            }
        }

        return dtos;
    }
}
