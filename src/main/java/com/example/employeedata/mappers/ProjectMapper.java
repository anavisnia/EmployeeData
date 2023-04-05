package com.example.employeedata.mappers;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.helpers.CustomPropValidators;
import com.example.employeedata.helpers.DateTimeHelpers;
import org.apache.logging.log4j.util.Strings;

public class ProjectMapper {
    public static final String errResource = "Project";

    public static Project mapToProject(CreateProjectDto projectDto) {
        Project project = new Project();

        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setCustomer(projectDto.getCustomer());
        project.setTeamSize(projectDto.getTeamSize());
        project.setDevLanguage(DevLanguage.values()[projectDto.getDevLanguage()]);
        project.setTerminationDate(projectDto.getTerminationDate());
        project.setCompletionDate(projectDto.getCompletionDate() == null ? null : projectDto.getCompletionDate());
        project.setModificationDate(DateTimeHelpers.getZDTFromLDTNow(projectDto.getTerminationDate().getZone().toString()));

        return project;
    }

    public static void mapToProject(Project existingProject, EditProjectDto projectDto) {
        existingProject.setTitle(projectDto.getTitle());
        existingProject.setDescription(projectDto.getDescription());
        existingProject.setCustomer(projectDto.getCustomer());
        existingProject.setTeamSize(projectDto.getTeamSize());
        existingProject.setDevLanguage(DevLanguage.values()[projectDto.getDevLanguage()]);
        existingProject.setTerminationDate(projectDto.getTerminationDate());
        existingProject.setCompletionDate(projectDto.getCompletionDate() == null ? null : projectDto.getCompletionDate());
        existingProject.setModificationDate(DateTimeHelpers.getZDTFromLDTNow(projectDto.getTerminationDate().getZone().toString()));
    }

    public static ProjectDto mapToProjectDto(Project project, String zoneId) {
        ProjectDto dto = new ProjectDto();

        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setCustomer(project.getCustomer());
        dto.setTeamSize(project.getTeamSize());
        dto.setTerminationDate(DateTimeHelpers.getLDTFromZDT(project.getTerminationDate(), zoneId));
        dto.setCompletionDate(project.getCompletionDate() == null ? null : DateTimeHelpers.getLDTFromZDT(project.getCompletionDate(), zoneId));
        dto.setDevLanguage(project.getDevLanguage().label);

        return dto;
    }

    public static Project mapToProject(String[] projectFieldsFromFile, String zoneId) {
        Project project = new Project();
        
        project.setTitle(projectFieldsFromFile[0].trim());
        project.setDescription(Strings.isNotBlank(projectFieldsFromFile[1]) ? projectFieldsFromFile[1].trim() : null);
        project.setCustomer(projectFieldsFromFile[2].trim());
        project.setTeamSize(Strings.isNotBlank(projectFieldsFromFile[3]) ? CustomPropValidators.validateTeamSize(Integer.parseInt(projectFieldsFromFile[3].trim().replace(".0", "")), errResource) : null);
        project.setDevLanguage(DevLanguage.values()[Integer.parseInt(projectFieldsFromFile[4].trim().replace(".0", ""))]);
        project.setTerminationDate(DateTimeHelpers.getFormattedZDTFromString(projectFieldsFromFile[5].trim(), zoneId));
        if (Strings.isNotBlank(projectFieldsFromFile[6])) {
            project.setCompletionDate(DateTimeHelpers.getFormattedZDTFromString(projectFieldsFromFile[6].trim(), zoneId));
        } else {
            project.setCompletionDate(null);
        }
        project.setModificationDate(DateTimeHelpers.getZDTFromLDTNow(zoneId));

        return project;
    }

    public static String[] mapToProjectArr(Project project) {
        String[] projectArr = {
                Strings.isBlank(project.getTitle()) ? "" : project.getTitle(),
                Strings.isBlank(project.getDescription()) ? "" : project.getDescription(),
                Strings.isBlank(project.getCustomer()) ? "" : project.getCustomer(),
                Strings.isBlank(project.getTeamSize().toString()) ? "" : project.getTeamSize().toString(),
                Strings.isBlank(project.getDevLanguage().label) ? "" : project.getDevLanguage().label,
                Strings.isBlank(project.getTerminationDate().toString()) ? "" : project.getTerminationDate().toString()
        };

        return projectArr;
    }
}
