package com.example.employeedata.MockData;

import com.example.employeedata.dto.CreateProjectDto;
import com.example.employeedata.dto.ProjectDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.mappers.ProjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
        dto.setTerminationDate(ZonedDateTime.now().plusYears(2L).withZoneSameInstant(ZoneId.of("Europe/Vilnius")));
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

    public static List<Project> validProjectDataRandom() {
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

    public static List<Project> validProjectData() {
        List<Project> list = new ArrayList<>();
        Project data1 = new Project();
        data1.setId((long) 1);
        data1.setTitle("Astrog web service project");
        data1.setDescription("Creating solution of web service");
        data1.setCustomer("Astrog");
        data1.setDevLanguage(DevLanguage.Unknown);
        data1.setTeamSize(0);
        data1.setTerminationDate(ZonedDateTime.now().plusYears(2L).withZoneSameInstant(ZoneId.of("Europe/Vilnius")));
        data1.setModificationDate(ZonedDateTime.now().minusMonths(2));
        list.add(data1);

        Project data2 = new Project();
        data2.setId((long) 2);
        data2.setTitle("Web site for Zoorm");
        data2.setDescription("Web site for Zoorm, build with Java");
        data2.setCustomer("Zoorm");
        data2.setDevLanguage(DevLanguage.Java);
        data2.setTeamSize(25);
        data2.setTerminationDate(ZonedDateTime.now().plusYears(4L).withZoneSameInstant(ZoneId.of("Europe/Vilnius")));
        data2.setModificationDate(ZonedDateTime.now().minusYears(1).minusMonths(2));
        list.add(data2);

        Project data3 = new Project();
        data3.setId((long) 3);
        data3.setTitle("Security solution for Frop");
        data3.setDescription("Security solution for Frop");
        data3.setCustomer("Frop");
        data3.setDevLanguage(DevLanguage.Go);
        data3.setTeamSize(40);
        data3.setTerminationDate(ZonedDateTime.parse("2021-01-01T18:00:00+03:00[Europe/Vilnius]"));
        data3.setModificationDate(ZonedDateTime.now().minusYears(3));
        data3.setCompletionDate(ZonedDateTime.parse("2019-12-27T17:00:00+03:00[Europe/Vilnius]"));
        list.add(data3);

        return list;
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
