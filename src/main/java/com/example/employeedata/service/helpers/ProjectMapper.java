package com.example.employeedata.service.helpers;

import com.example.employeedata.dto.ProjectDto;
import com.example.employeedata.entity.Project;

public class ProjectMapper {
    public static String errResource = "Project";

    public static Project mapToProject(ProjectDto projectDto) {
        Project project = new Project();

        project.setTitle(projectDto.getTitle());
        project.setCustomer(projectDto.getCustomer());
        project.setTeamSize(projectDto.getTeamSize());
        project.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        project.setTerminationDate(projectDto.getTerminationDate());
        return project;
    }

    public static Project mapToProject(Project existingProject, ProjectDto projectDto) {
        existingProject.setId(existingProject.getId());
        existingProject.setTitle(projectDto.getTitle());
        existingProject.setCustomer(projectDto.getCustomer());
        existingProject.setTeamSize(projectDto.getTeamSize());
        existingProject.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource));
        existingProject.setTerminationDate(projectDto.getTerminationDate());
        
        return existingProject;
    }
}
