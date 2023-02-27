package com.example.employeedata.mappers;

import com.example.employeedata.dto.ProjectFileDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.helpers.DateTimeHelpers;

public final class ProjectFileMapper {
    public static ProjectFileDto mapToProjectFileDto(Project project, String zoneId) {
        ProjectFileDto projectFileDto = new ProjectFileDto();

        projectFileDto.setTitle(project.getTitle());
        projectFileDto.setDescription(project.getDescription());
        projectFileDto.setCustomer(project.getCustomer());
        projectFileDto.setTeamSize(project.getTeamSize().toString());
        projectFileDto.setDevLanguage(DevLanguage.values()[project.getDevLanguage().ordinal()].label);
        projectFileDto.setTerminationDate(DateTimeHelpers.GetLDTFromZDT(project.getTerminationDate(), zoneId).toString());
        projectFileDto.setCompletionDate(DateTimeHelpers.GetLDTFromZDT(project.getCompletionDate(), zoneId).toString());

        return projectFileDto;
    }
}
