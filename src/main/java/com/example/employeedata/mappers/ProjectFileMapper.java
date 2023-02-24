package com.example.employeedata.mappers;

import com.example.employeedata.dto.ProjectFileDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;

public final class ProjectFileMapper {
    public static ProjectFileDto mapToProjectFileDto(Project project) {
        ProjectFileDto projectFileDto = new ProjectFileDto();

        projectFileDto.setTitle(project.getTitle());
        projectFileDto.setDescription(project.getDescription());
        projectFileDto.setCustomer(project.getCustomer());
        projectFileDto.setTeamSize(project.getTeamSize().toString());
        projectFileDto.setDevLanguage(DevLanguage.values()[project.getDevLanguage().ordinal()].label);
        projectFileDto.setTerminationDate(project.getTerminationDate().toString());
        projectFileDto.setCompletionDate(project.getCompletionDate().toString());

        return projectFileDto;
    }
}
