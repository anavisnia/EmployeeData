package com.example.employeedata.mappers;

import com.example.employeedata.dto.ProjectFileDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.helpers.DateTimeHelpers;
import org.apache.logging.log4j.util.Strings;

public final class ProjectFileMapper {
    public static ProjectFileDto mapToProjectFileDto(Project project, String zoneId) {
        ProjectFileDto projectFileDto = new ProjectFileDto();

        projectFileDto.setTitle(project.getTitle());
        projectFileDto.setDescription(Strings.isNotBlank(project.getDescription()) ? project.getDescription() : "");
        projectFileDto.setCustomer(project.getCustomer());
        projectFileDto.setTeamSize(project.getTeamSize() != null ? project.getTeamSize().toString() : "");
        projectFileDto.setDevLanguage(DevLanguage.values()[project.getDevLanguage().ordinal()].label);
        projectFileDto.setTerminationDate(DateTimeHelpers.GetLDTFromZDT(project.getTerminationDate(), zoneId).toString());
        projectFileDto.setCompletionDate(project.getCompletionDate() != null ? DateTimeHelpers.GetLDTFromZDT(project.getCompletionDate(), zoneId).toString() : "");

        return projectFileDto;
    }
}
