package com.example.employeedata.service.impl;

import java.io.*;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.*;

import java.sql.SQLException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.enums.FileTypes;
import com.example.employeedata.exception.*;
import com.example.employeedata.helpers.*;
import com.example.employeedata.mappers.*;
import com.example.employeedata.repository.*;
import com.example.employeedata.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final Validator validator;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private static final String RES_NAME = "Project";
    private static final String ID = "id";

    public ProjectServiceImpl(Validator validator,
            ProjectRepository projectRepository,
            EmployeeRepository employeeRepository) {
        this.validator = validator;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, SQLException.class})
    public ResponseDto saveProject(CreateProjectDto projectDto) {
        constraintViolationCheck(projectDto);
        ProjectValidator.validateCreateDto(projectDto);

        Project dbResponse = projectRepository.save(ProjectMapper.mapToProject(projectDto));

        return new ResponseDto(dbResponse.getId(), RES_NAME, false);
    }

    @Override
    public ResponseDto saveProjectsFromExelFile(MultipartFile multipartFile, String zoneId) {
        if (multipartFile == null || Strings.isBlank(multipartFile.getOriginalFilename()) || multipartFile.isEmpty()) {
            throw new CustomValidationException("File", "File and/or file name cannot be null or empty");
        }
        FileHelperFunctions.isProperFileType(multipartFile.getOriginalFilename());

        DateTimeHelpers.validateZoneId(zoneId);

        File file = FileHelperFunctions.castMultipartFileToFile(multipartFile);
        String fileType = FileHelperFunctions.getExtensionFromFileName(file.getName());
        Set<Project> createProjects = new HashSet<>();
        Set<String[]> failedValidationEntities = new HashSet<>();

        try(FileInputStream fileBytes = new FileInputStream(file)) {
            Iterator<Row> rows = null;

            if (FileTypes.Xls.label.equalsIgnoreCase(fileType)) {
                try (HSSFWorkbook xlsWorkBook = new HSSFWorkbook(fileBytes)) {
                    HSSFSheet workBookSheet = xlsWorkBook.getSheetAt(0);
                    rows = workBookSheet.iterator();
                }
            } else if(FileTypes.Xlsx.label.equalsIgnoreCase(fileType)) {
                try (XSSFWorkbook xlsxWorkBook = new XSSFWorkbook(fileBytes)) {
                    XSSFSheet workBookSheet = xlsxWorkBook.getSheetAt(0);
                    rows = workBookSheet.iterator();
                }
            }

            //to skip first row which contains additional information about cells
            Row row;
            if (rows != null && rows.hasNext()) {
                row = rows.next(); // row number is 0
            }

            while (rows != null && rows.hasNext()) {
                row = rows.next();

                String[] projectData = new String[Constants.PROJECT_FILE_HEADERS.length];

                int cellCount = 0;
                while (cellCount < projectData.length) {
                    Cell cell = row.getCell(cellCount);

                    String cellValue;

                    cellValue = FileHelperFunctions.getCellValue(cell);

                    projectData[cellCount] = cellValue.trim();

                    cellCount++;
                }

                if (!CustomPropValidators.areAllFieldsEmpty(projectData)) {
                    if (ProjectValidator.isValidProjectFile(projectData, zoneId)) {
                        createProjects.add(ProjectMapper.mapToProject(projectData, zoneId));
                    } else {
                        failedValidationEntities.add(projectData);
                    }
                }
            }
        } catch (IOException e) {
            //will throw Internal Server Error

        } finally {
            //deleting temp file
            if (file.exists()) {
                file.delete();
            }
        }

        List<Project> checkForDuplicates = new ArrayList<>();

        for (Project proj : createProjects) {
            Project project = projectRepository.findByTitleCustomerDevLang(proj.getTitle() ,proj.getCustomer(), proj.getDevLanguage().ordinal())
                    .orElse(null);
            if (project != null) {
                checkForDuplicates.add(proj);
            }
        }

        if (!checkForDuplicates.isEmpty()) {
            checkForDuplicates.forEach(createProjects::remove);
            checkForDuplicates.forEach(project -> project.setTitle("duplicate project: " + project.getTitle()));
            failedValidationEntities.addAll(checkForDuplicates.stream().map(ProjectMapper::mapToProjectArr).collect(Collectors.toList()));
        }

        ResponseDto response;

        List<Long> dbResponse = projectRepository.saveAll(createProjects)
            .stream()
            .filter(Objects::nonNull)
            .map(Project::getId)
            .collect(Collectors.toList());

        if (createProjects.isEmpty() && !failedValidationEntities.isEmpty()) {
            response = new ResponseDto(
                failedValidationEntities,
                "Project/projects",
                " error. No entities to save into database",
                true);
        } else if(!failedValidationEntities.isEmpty()) {
            
            response = new ResponseDto(
                dbResponse, "Project/projects created successfully",
                failedValidationEntities, "These rows contain errors. Please check"
            );
        }
        else {
            response = new ResponseDto(dbResponse, "Project/projects", false);
        }

        return response;
    }

    @Override
    public List<ProjectDto> getAllProjects(String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        return projectRepository.findAll()
            .stream()
            .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
            .collect(Collectors.toList());
    }

    @Override
    public PaginatedResponseDto<ProjectDto> getAllProjectsPage(
            String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc, String zoneId
    ) {
        DateTimeHelpers.validateZoneId(zoneId);

        if (pageNumber == null || pageNumber < 0) {
            throw new CustomValidationException("pageNumber", "It cannot be null or less than zero");
        }

        pageSize = HelperFunctions.checkPageSize(pageSize);

        String[] fields = Strings.isNotBlank(searchQuery) ? Constants.PROJECT_DB_FIELDS : Constants.PROJECT_FIELDS;
        String sorting = HelperFunctions.checkFieldSorting(fields, sortBy);
        Pageable paging = HelperFunctions.returnPageableWithSorting(pageNumber, pageSize, sorting, isAsc);
        Page<Project> result;

        if (Strings.isNotBlank(searchQuery)) {
            result = projectRepository.findAllByQuery(searchQuery + "%", searchQuery, paging);
        } else {
            result = projectRepository.findAll(paging);
        }
        
        if (!result.hasContent()) {
            return new PaginatedResponseDto<>();
        }

        return new PaginatedResponseDto<>(
                result.getContent().stream().map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                pageNumber
        );
    }

    @Override
    public List<ProjectDto> getAllProjectsWithFutureTerminationDate(String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        return projectRepository.findByFutureTerminationDate(DateTimeHelpers.getLocalDateNow())
            .stream()
            .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getAllProjectsWithPriorTerminationDate(String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        return projectRepository.findByPriorTerminationDate(DateTimeHelpers.getLocalDateNow())
            .stream()
            .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId, String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, employeeId)
        );

        List<Long> employeeProjectIds = getProjectIds(new ArrayList<>(employee.getProjects()));
        List<Project> projects = projectRepository.findByFutureTerminationDate(LocalDate.now());

        projects.removeIf(p -> employeeProjectIds.contains(p.getId()));

        return projects
            .stream()
            .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date, String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, employeeId)
        );
        
        LocalDate currentDate = DateTimeHelpers.getLocalDateNow();

        if (date.getYear() < currentDate.getYear()||
            (date.getYear() == currentDate.getYear() && date.getMonthValue() < currentDate.getMonthValue())) {
            throw new CustomValidationException("Date", "date", date, "Date cannot be in past time");
        }

        List<Long> employeeProjectIds = getProjectIds(new ArrayList<>(employee.getProjects()));
        List<Project> projects = projectRepository.findByFutureTerminationDate(date);

        projects.removeIf(p -> employeeProjectIds.contains(p.getId()));

        return projects.stream().map(p -> ProjectMapper.mapToProjectDto(p, zoneId)).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getAllProjectsByDevelopmentLanguage(Integer devLanguage, String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);
        CustomPropValidators.validateDevLang(devLanguage, RES_NAME);

        return projectRepository.findByDevLanguage(devLanguage)
            .stream()
            .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
            .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProjectById(String projectId, String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        return ProjectMapper.mapToProjectDto(
            projectRepository.findById(Long.parseLong(projectId)).orElseThrow(() ->
                new ResourceNotFoundException(RES_NAME, ID, projectId)
            ),
            zoneId
        );
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, SQLException.class})
    public void updateProject(Long projectId, EditProjectDto projectDto) {
        constraintViolationCheck(projectDto);
        ProjectValidator.validateEditDto(projectDto);

        Project existingProject = projectRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, projectId)
        );

        DateTimeHelpers.validateZoneId(projectDto.getTerminationDate().getZone().getId());

        ProjectMapper.mapToProject(existingProject, projectDto);

        projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project existingProject = projectRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, projectId)
        );

        removeProjectsFromEmployees(projectId, existingProject);
        
        projectRepository.delete(existingProject);
    }

    @Override
    public byte[] generateExelFile(String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        try(Workbook workBook = new XSSFWorkbook()) {
            
            LocalDate date = DateTimeHelpers.getLocalDateNow();
            Sheet workBookSheet = workBook.createSheet(Constants.PROJECT_FILE_NAME + date);

            workBookSheet.setDefaultColumnWidth(100000);
            workBookSheet.setDefaultRowHeight((short) 500);
            
            Row header = workBookSheet.createRow(0);
            FileHelperFunctions.populateHeaderRow(header, Constants.PROJECT_FILE_HEADERS);

            List<Project> data = projectRepository.findAll();
                 
            if (!data.isEmpty()) {
                List<ProjectFileDto> projectsData = data
                        .stream()
                        .map(p -> ProjectFileMapper.mapToProjectFileDto(p, zoneId))
                        .collect(Collectors.toList());

                Row row;
                Cell cell;

                for (int i = 1; i <= projectsData.size(); i++) {
                    row = workBookSheet.createRow(i);
                    ProjectFileDto project = projectsData.get(i-1);
                    for (int j = 0; j < Constants.PROJECT_FILE_HEADERS.length; j++) {
                        cell = row.createCell(j);
                        cell.setCellValue(getProjectDataAtIndex(j, project));
                    }
                }
            }

            try {
                workBook.write(bao);
                workBook.close();
            } finally {
                bao.close();
            }
        } catch (IOException e) {
            // will throw Internal Server Error
        }

        return bao.toByteArray();
    }

    @Override
    public Map<String, List<ProjectDto>> getProjectsGroupedByDevLanguage(String zoneId) {
        DateTimeHelpers.validateZoneId(zoneId);

        return projectRepository.findAll()
            .stream()
            .map(p -> ProjectMapper.mapToProjectDto(p, zoneId))
            .collect(Collectors.groupingBy(ProjectDto::getDevLanguage));
    }

    @Override
    public Map<String, String> getZoneIdsWithOffset(String isRegionSort) {
        Map<String, String> zoneIdsWithOffset = new HashMap<>();
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        LocalDateTime ldt = DateTimeHelpers.getLocalDateTimeNow();

        for (String zoneId : zoneIds) {
            ZoneId zId = ZoneId.of(zoneId);

            ZonedDateTime zdt = DateTimeHelpers.getZDTFromLDT(ldt, zoneId);

            ZoneOffset zo = zdt.getOffset();

            String offset = zo.getId().replaceAll("Z", "+00:00");

            zoneIdsWithOffset.put(zId.toString(), offset);
        }

        Map<String, String> sorted = new LinkedHashMap<>();

        if (Boolean.parseBoolean(isRegionSort)) {
            zoneIdsWithOffset
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> sorted.put(e.getKey(), e.getValue()));
        } else {
            zoneIdsWithOffset
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, String>comparingByValue().reversed())
                .forEachOrdered(e -> sorted.put(e.getKey(), e.getValue()));
        }

        return sorted;
    }

    @PreRemove
    @Transactional(rollbackFor = {RuntimeException.class, SQLException.class})
    private void removeProjectsFromEmployees(Long projectId, Project project) {
        List<Employee> employees = employeeRepository.findByProjectId(projectId);

        employees.forEach(e -> e.getProjects().remove(project));
        
        employeeRepository.saveAll(employees);
    }

    private List<Long> getProjectIds(List<Project> projects) {
        return projects
            .stream()
            .map(Project::getId)
            .collect(Collectors.toList());
    }

    private <T> void constraintViolationCheck(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            violations.forEach(cv -> sb.append(cv.getPropertyPath() + " " + cv.getMessage() + ". "));

            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }
    }
    
    private String getProjectDataAtIndex(Integer index, ProjectFileDto project) {
        String dataAtIndex;

        switch(index) {
            case 0:
                dataAtIndex = project.getTitle();
                break;
            case 1:
                dataAtIndex = project.getDescription();
                break;
            case 2:
                dataAtIndex = project.getCustomer();
                break;
            case 3:
                dataAtIndex = project.getTeamSize();
                break;
            case 4:
                dataAtIndex = project.getDevLanguage();
                break;
            case 5:
                dataAtIndex = project.getTerminationDate();
                break;
            case 6:
                dataAtIndex = project.getCompletionDate();
                break;
            default:
                dataAtIndex = "";
        }

        return dataAtIndex;
    }
}
