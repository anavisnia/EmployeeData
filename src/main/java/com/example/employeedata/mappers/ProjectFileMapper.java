package com.example.employeedata.mappers;

import com.example.employeedata.dto.ProjectFileDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import org.apache.logging.log4j.util.Strings;

import java.time.ZoneId;

public final class ProjectFileMapper {
    public static ProjectFileDto mapToProjectFileDto(Project project, String zoneId) {
        ProjectFileDto projectFileDto = new ProjectFileDto();

        projectFileDto.setTitle(project.getTitle());
        projectFileDto.setDescription(Strings.isNotBlank(project.getDescription()) ? project.getDescription() : "");
        projectFileDto.setCustomer(project.getCustomer());
        projectFileDto.setTeamSize(project.getTeamSize() != null ? project.getTeamSize().toString() : "");
        projectFileDto.setDevLanguage(DevLanguage.values()[project.getDevLanguage().ordinal()].label);
        projectFileDto.setTerminationDate(project.getTerminationDate().withZoneSameInstant(ZoneId.of(zoneId)).toString());
        projectFileDto.setCompletionDate(project.getCompletionDate() != null ? project.getCompletionDate().withZoneSameInstant(ZoneId.of(zoneId)).toString() : "");

        return projectFileDto;
    }
}
