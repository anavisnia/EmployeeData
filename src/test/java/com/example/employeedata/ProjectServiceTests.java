package com.example.employeedata;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.employeedata.MockData.EmployeeData;
import com.example.employeedata.MockData.ProjectData;
import com.example.employeedata.dto.*;
import com.example.employeedata.entity.Project;
import com.example.employeedata.exception.CustomValidationException;
import com.example.employeedata.helpers.Constants;
import com.example.employeedata.helpers.DateTimeHelpers;
import com.example.employeedata.helpers.HelperFunctions;
import com.example.employeedata.mappers.ProjectMapper;
import com.example.employeedata.repository.EmployeeRepository;
import com.example.employeedata.repository.ProjectRepository;
import com.example.employeedata.service.ProjectService;
import com.example.employeedata.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        List<Project> entityData = ProjectData.validProjectDataRandom();

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

    @ParameterizedTest
    @CsvSource(value = "NULL, 0, 10, 0, true, Europe/Vilnius", nullValues = "NULL")
    public void getAllProjectsPage_whenPassingValidParamsWithoutSearchQuery_ReturnsPaginatedResponseAscendingOrder(
            String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc, String zoneId
    ) {
        List<Project> entityData = ProjectData.validProjectDataRandom();
        List<ProjectDto> dtoData = entityData.stream().map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList());
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(Constants.PROJECT_FIELDS[sortBy]).ascending());
        Page<Project> result = new PageImpl<>(entityData, paging, entityData.size());

        when(projectRepository.findAll(paging)).thenReturn(result);

        PaginatedResponseDto<ProjectDto> expectedResponse = new PaginatedResponseDto<>(dtoData, result.getTotalElements(), result.getTotalPages(), pageNumber);
        PaginatedResponseDto<ProjectDto> actualResponse = projectService.getAllProjectsPage(searchQuery, pageNumber, pageSize, sortBy, isAsc, zoneId);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @CsvSource(value = "NULL, 0, 10, 0, false, Europe/Vilnius", nullValues = "NULL")
    public void getAllProjectsPage_whenPassingValidParamsWithoutSearchQuery_ReturnsPaginatedResponseDescendingOrder(
            String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc, String zoneId
    ) {
        List<Project> entityData = ProjectData.validProjectDataRandom();
        List<ProjectDto> dtoData = entityData.stream().map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList());
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(Constants.PROJECT_FIELDS[sortBy]).descending());
        Page<Project> result = new PageImpl<>(entityData, paging, entityData.size());

        when(projectRepository.findAll(paging)).thenReturn(result);

        PaginatedResponseDto<ProjectDto> expectedResponse = new PaginatedResponseDto<>(dtoData, result.getTotalElements(), result.getTotalPages(), pageNumber);
        PaginatedResponseDto<ProjectDto> actualResponse = projectService.getAllProjectsPage(searchQuery, pageNumber, pageSize, sortBy, isAsc, zoneId);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @CsvSource("Project, 0, 10, 0, true, Europe/Vilnius")
    public void getAllProjectsPage_whenPassingValidParamsWithValidSearchQuery_ReturnsPaginatedResponseAscendingOrder(
            String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc, String zoneId
    ) {
        List<Project> entityData = ProjectData.validProjectDataRandom().stream()
                .filter(p -> p.getTitle().contains(searchQuery) ||
                        p.getDescription().contains(searchQuery) ||
                        p.getCustomer().contains(searchQuery))
                .collect(Collectors.toList());
        List<ProjectDto> dtoData = entityData.stream().map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList());
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(Constants.PROJECT_DB_FIELDS[sortBy]).ascending());
        Page<Project> result = new PageImpl<>(entityData, paging, entityData.size());

        when(projectRepository.findAllByQuery(searchQuery + "%", searchQuery, paging)).thenReturn(result);

        PaginatedResponseDto<ProjectDto> expectedResponse = new PaginatedResponseDto<>(dtoData, result.getTotalElements(), result.getTotalPages(), pageNumber);
        PaginatedResponseDto<ProjectDto> actualResponse = projectService.getAllProjectsPage(searchQuery, pageNumber, pageSize, sortBy, isAsc, zoneId);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @CsvSource("Astrog, 0, 10, 0, false, Europe/Vilnius")
    public void getAllProjectsPage_whenPassingValidParamsWithValidSearchQuery_ReturnsPaginatedResponseDescendingOrder(
            String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc, String zoneId
    ) {
        List<Project> entityData = ProjectData.validProjectDataRandom().stream()
                .filter(p -> p.getTitle().contains(searchQuery) ||
                        p.getDescription().contains(searchQuery) ||
                        p.getCustomer().contains(searchQuery))
                .collect(Collectors.toList());
        List<ProjectDto> dtoData = entityData.stream().map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList());
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(Constants.PROJECT_DB_FIELDS[sortBy]).descending());
        Page<Project> result = new PageImpl<>(entityData, paging, entityData.size());

        when(projectRepository.findAllByQuery(searchQuery + "%", searchQuery, paging)).thenReturn(result);

        PaginatedResponseDto<ProjectDto> expectedResponse = new PaginatedResponseDto<>(dtoData, result.getTotalElements(), result.getTotalPages(), pageNumber);
        PaginatedResponseDto<ProjectDto> actualResponse = projectService.getAllProjectsPage(searchQuery, pageNumber, pageSize, sortBy, isAsc, zoneId);

        assertThat(actualResponse.getNumberOfItems()).isEqualTo(expectedResponse.getNumberOfItems());
        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Europe/Vil", "Turk", "Asiaaa"})
    public void getAllProjectsPage_whenPassingWrongZoneId_ThrowsCustomValidationException(String zoneId) {
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> projectService.getAllProjectsPage("Astrog", 0, 10, 0, "true", zoneId));
        String expectedErrMessage = "Validation error for zoneId/zoneOffset in zoneId/zoneOffset property. Was entered: '" + zoneId + "'.";

        assertEquals(CustomValidationException.class, ex.getClass());
        assertEquals(expectedErrMessage, ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, -1000, 0})
    public void getAllProjectsPage_whenPassingWrongPageNumber_ThrowsCustomValidationException(int pageNumber) {
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> projectService.getAllProjectsPage("Astrog", pageNumber == 0 ? null : pageNumber, 10, 0, "true", "Europe/Vilnius"));
        String expectedErrMessage = "Validation error for pageNumber. It cannot be null or less than zero.";

        assertEquals(CustomValidationException.class, ex.getClass());
        assertEquals(expectedErrMessage, ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, -1000, 0, -1})
    public void getAllProjectsPage_whenPassingWrongPageSizeNullOrSmallerThanZero_ReturnsValidSmallestNumber(int pageSize) {
        int actualPageSize = 10;
        int expectedPageSize = HelperFunctions.checkPageSize(pageSize == -1 ? null : pageSize);

        assertEquals(expectedPageSize, actualPageSize);
    }

    @ParameterizedTest
    @ValueSource(ints = {700, 501})
    public void getAllProjectsPage_whenPassingWrongPageSizeMoreThan500_ReturnsValidLargestNumber(int pageSize) {
        int actualPageSize = 500;
        int expectedPageSize = HelperFunctions.checkPageSize(pageSize);

        assertEquals(expectedPageSize, actualPageSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Turkey", "Europe/Vilnius"})
    public void getAllProjectsWithFutureTerminationDate_whenPassingValidZoneId_returnsListOfData(String zoneId) {
        List<Project> projectsEntity = ProjectData.validProjectDataRandom();
        List<ProjectDto> actualResult = projectsEntity.stream()
                .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
                .filter(p -> p.getTerminationDate().isAfter(LocalDate.now().atStartOfDay()))
                .collect(Collectors.toList());

        when(projectRepository.findByFutureTerminationDate(LocalDate.now())).thenReturn(projectsEntity);

        List<ProjectDto> expectedResult = projectService.getAllProjectsWithFutureTerminationDate(zoneId);

        assertThat(expectedResult).usingRecursiveComparison().isEqualTo(actualResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Turkey", "Europe/Vilnius"})
    public void getAllProjectsWithFutureTerminationDate_whenPassingValidZoneId_returnsEmptyList(String zoneId) {
        when(projectRepository.findByFutureTerminationDate(LocalDate.now())).thenReturn(new ArrayList<>());

        List<ProjectDto> expectedResult = projectService.getAllProjectsWithFutureTerminationDate(zoneId);

        assertThat(expectedResult).usingRecursiveComparison().isEqualTo(new ArrayList<>());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Tu", "Eur"})
    public void getAllProjectsWithFutureTerminationDate_whenPassingNotValidZoneId_throwsCustomValidationError(String zoneId) {
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> projectService.getAllProjectsWithFutureTerminationDate(zoneId));

        assertEquals(ex.getClass(), CustomValidationException.class);

        String expectedErrMessage  = "Validation error for zoneId/zoneOffset in zoneId/zoneOffset property. Was entered: '" + zoneId +"'.";
        assertThat(ex.getMessage()).isEqualTo(expectedErrMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Turkey", "Europe/Vilnius"})
    public void getAllProjectsWithPriorTerminationDate_whenPassingValidZoneId_returnsListOfData(String zoneId) {
        List<Project> projectsEntity = ProjectData.validProjectDataRandom();
        List<ProjectDto> actualResult = projectsEntity.stream()
                .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
                .filter(p -> p.getTerminationDate().isBefore(LocalDate.now().atStartOfDay()))
                .collect(Collectors.toList());

        when(projectRepository.findByPriorTerminationDate(LocalDate.now()))
                .thenReturn(projectsEntity.
                        stream().filter(p -> p.getTerminationDate().isBefore(LocalDate.now().atStartOfDay(ZoneId.of(zoneId))))
                        .collect(Collectors.toList()));

        List<ProjectDto> expectedResult = projectService.getAllProjectsWithFutureTerminationDate(zoneId);

        assertThat(expectedResult).usingRecursiveComparison().isEqualTo(actualResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Turkey", "Europe/Vilnius"})
    public void getAllProjectsWithFuturePriorDate_whenPassingValidZoneId_returnsEmptyList(String zoneId) {
        when(projectRepository.findByPriorTerminationDate(LocalDate.now())).thenReturn(new ArrayList<>());

        List<ProjectDto> expectedResult = projectService.getAllProjectsWithPriorTerminationDate(zoneId);

        assertThat(expectedResult).usingRecursiveComparison().isEqualTo(new ArrayList<>());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Assia", "Europe/"})
    public void getAllProjectsWithPriorTerminationDate_whenPassingNotValidZoneId_throwsCustomValidationError(String zoneId) {
        Exception ex = assertThrowsExactly(CustomValidationException.class, () -> projectService.getAllProjectsWithPriorTerminationDate(zoneId));

        assertEquals(ex.getClass(), CustomValidationException.class);

        String expectedErrMessage  = "Validation error for zoneId/zoneOffset in zoneId/zoneOffset property. Was entered: '" + zoneId +"'.";
        assertThat(ex.getMessage()).isEqualTo(expectedErrMessage);
    }

    @ParameterizedTest
    @CsvSource("2, Turkey")
    public void getAllProjectsNotAssignedToEmployeeFromCurrentDate_whenPassingValidData_doesNotThrow(Long employeeId, String zoneId) {
        when(employeeRepository.findById(anyLong()))
                .thenReturn(EmployeeData.validEmployeeData().stream().filter(e -> Objects.equals(e.getId(), employeeId)).findFirst());

        List<Project> projectData = ProjectData.validProjectData().stream()
                .filter(p -> p.getTerminationDate().isAfter(ZonedDateTime.now()))
                .collect(Collectors.toList());

        when(projectRepository.findByFutureTerminationDate(LocalDate.now())).thenReturn(projectData);

        assertThatCode(() -> projectService.getAllProjectsNotAssignedToEmployeeFromCurrentDate(employeeId, zoneId))
                .doesNotThrowAnyException();
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
