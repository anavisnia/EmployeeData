package com.example.employeedata.MockData;

import com.example.employeedata.dto.CreateProjectDto;
import com.example.employeedata.dto.ProjectDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.mappers.ProjectMapper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public final class ProjectData {
    public static CreateProjectDto validCreateProjectDto() {
        CreateProjectDto dto = new CreateProjectDto();
        dto.setTitle("Astrog web service project");
        dto.setDescription("Creating solution of web service");
        dto.setCustomer("Astrog");
        dto.setTeamSize(0);
        dto.setDevLanguage(0);
        dto.setTerminationDate(ZonedDateTime.parse("2025-01-01T17:00:00+03:00[Europe/Vilnius]"));
        return dto;
    }

    public static Project validProject() {
        Project project = new Project();
        project.setId(1L);
        project.setTitle(validCreateProjectDto().getTitle());
        project.setDescription(validCreateProjectDto().getDescription());
        project.setCustomer(validCreateProjectDto().getCustomer());
        project.setTeamSize(validCreateProjectDto().getTeamSize());
        project.setDevLanguage(DevLanguage.values()[validCreateProjectDto().getDevLanguage()]);
        project.setTerminationDate(validCreateProjectDto().getTerminationDate());
        project.setModificationDate(ZonedDateTime.now());
        return project;
    }

    public static ProjectDto validProjectDto() {
        Project project  = validProject();
        return ProjectMapper.mapToProjectDto(project, project.getTerminationDate().getZone().getId());
    }

    public static List<Project> validProjectData() {
        List<Project> data = new ArrayList<>();
        Random randomNum = new Random();
        for (int i = 1; i <= 5; i++) {
            if (i == 1) {
                data.add(validProject());
            }
            Project dto = new Project((long) i, "Project" + i, "Description" + i, randomNum.nextInt(10), "AStrom", ZonedDateTime.now().plusYears(2L), null, DevLanguage.values()[randomNum.nextInt(DevLanguage.size() - 1)], ZonedDateTime.now(), new HashSet<>());
            data.add(dto);
        }
        return data;
    }

    public static List<ProjectDto> validProjectDtoData() {
        List<ProjectDto> data = new ArrayList<>();
        Random randomNum = new Random();
        for (int i = 1; i <= 5; i++) {
            if (i == 1) {
                data.add(validProjectDto());
            }
            ProjectDto dto = new ProjectDto("Project" + i, "Description" + i, randomNum.nextInt(10), "AStrom", LocalDateTime.now().plusYears(2L), null, DevLanguage.values()[randomNum.nextInt(DevLanguage.size() - 1)].label);
            data.add(dto);
        }
        return data;
    }

    public static CreateProjectDto notValidCreateProjectDto() {
        CreateProjectDto dto = new CreateProjectDto();
        dto.setTitle("");
        dto.setDescription("");
        dto.setCustomer("Astrog");
        dto.setTeamSize(1000);
        dto.setDevLanguage(5);
        dto.setTerminationDate(null);
        return dto;
    }
}
