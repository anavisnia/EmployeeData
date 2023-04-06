package com.example.employeedata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.employeedata.MockData.ProjectData;
import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Project;
import com.example.employeedata.exception.CustomValidationException;
import com.example.employeedata.helpers.DateTimeHelpers;
import com.example.employeedata.mappers.ProjectMapper;
import com.example.employeedata.repository.EmployeeRepository;
import com.example.employeedata.repository.ProjectRepository;
import com.example.employeedata.service.ProjectService;
import com.example.employeedata.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.*;

@RunWith(MockitoJUnitRunner.class)
class ProjectServiceTests {
    private ProjectRepository projectRepository;
    private EmployeeRepository employeeRepository;
    private ProjectService projectService;

    private final static String RES_NAME = "Project";

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        projectRepository = Mockito.mock(ProjectRepository.class);
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        projectService = new ProjectServiceImpl(validator, projectRepository, employeeRepository);
    }

    @Test
    public void saveProject_whenGivenValidData_ReturnsValidResponseDto() {
        CreateProjectDto dto = ProjectData.validCreateProjectDto();
        Project project = ProjectData.validProject();

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ResponseDto responseToCheck = new ResponseDto(project.getId(), RES_NAME, false);
        ResponseDto actualResponse = projectService.saveProject(dto);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(responseToCheck);
    }

    @ParameterizedTest
    @ValueSource(ints = {1000, 100, 220})
    public void saveProject_whenGivenNotValidDevLang_CustomValidationExceptionThrowsError(int devLang) {
        CreateProjectDto dto = ProjectData.validCreateProjectDto();
        dto.setDevLanguage(devLang);
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> projectService.saveProject(dto));

        String expectedMessage = "Validation error for Project in developer language property. Was entered: " + "'" + devLang + "'" + ". Project's development language cannot be out of scope.";
        String actualMessage = ex.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void saveProject_whenGivenNotValidDataBasedOnAnnotations_ConstraintViolationExceptionThrown() {
        CreateProjectDto dto = ProjectData.notValidCreateProjectDto();
        Exception ex = assertThrowsExactly(ConstraintViolationException.class, () -> projectService.saveProject(dto));

        assertSame(ex.getClass(), ConstraintViolationException.class);
    }

    @Test
    public void whenGivenNotValidZoneId_CustomValidationExceptionThrown() {
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> DateTimeHelpers.validateZoneId("tuurkey"));
        assertSame(ex.getClass(), CustomValidationException.class);
    }

    @Test
    public void getAllProjects_whenThereAreData_ReturnsAListOfProjectDto() {
        String zoneId = "Europe/Vilnius";
        List<Project> entityData = ProjectData.validProjectData();

        List<ProjectDto> dtoData = entityData.stream()
                .map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList());

        when(projectRepository.findAll()).thenReturn(entityData);

        List<ProjectDto> actualList = projectService.getAllProjects(zoneId);

        assertThat(dtoData).isNotNull();
        assertThat(dtoData).isNotEmpty();
        assertThat(actualList).usingRecursiveComparison().isEqualTo(dtoData);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Europe/Vil", "Turk"})
    public void getAllProjects_whenPassedWrongZoneId_CustomValidationExceptionThrown(String zoneId) {
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> projectService.getAllProjects(zoneId));
        assertThat(ex.getClass()).isEqualTo(CustomValidationException.class);
    }

    @Test
    public void getAllProjects_whenThereAreNoData_ReturnsAnEmptyList() {
        String zoneId = "Europe/Vilnius";

        when(projectRepository.findAll()).thenReturn(new ArrayList<>(0));

        List<ProjectDto> actualList = projectService.getAllProjects(zoneId);

        assertThat(actualList).isNotNull();
        assertThat(actualList).isEmpty();
    }

    @Test
    public void givenTimeZone_whenFetchingData_thenReturnsAllProjectWithSetTimeZone() {
        ZoneOffset zoneOffset = ZoneOffset.of("+02:00");
        String initialDate = "2029-01-01T18:00:00+01:00";
        String expectedDate = "2029-01-01T19:00:00+02:00";

        ZonedDateTime initialParsedAndZoneAdded = ZonedDateTime.parse(initialDate).withZoneSameInstant(zoneOffset);
        ZonedDateTime expectedOutcome = ZonedDateTime.parse(expectedDate);

        assertEquals(expectedOutcome, initialParsedAndZoneAdded);
    }

}
