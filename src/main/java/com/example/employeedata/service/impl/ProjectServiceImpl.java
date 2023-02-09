package com.example.employeedata.service.impl;

import java.io.*;
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
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
    private final String resourceName = "Project";

    public ProjectServiceImpl(Validator validator,
            ProjectRepository projectRepository,
            EmployeeRepository employeeRepository) {
        this.validator = validator;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ResponseDto saveProject(CreateProjectDto projectDto) {
        constraintViolationCheck(projectDto);

        Project project = ProjectMapper.mapToProject(projectDto);
        
        Project dbResponse = projectRepository.save(project);

        return new ResponseDto(dbResponse.getId(), resourceName, false);
    }

    @Override
    public ResponseDto saveProjectsFromExelFile(MultipartFile multipartFile) {
        if (multipartFile == null || (multipartFile != null && multipartFile.getOriginalFilename().isBlank()) ) {
            throw new CustomValidationException("File", "File and/or file name cannot be null or empty");
        }
        CustomPropValidators.isProperFileType(multipartFile.getOriginalFilename());

        File file = FileHelperFunctions.castMultipartFileToFile(multipartFile);
        String fileType = FileHelperFunctions.getExtensionFromFileName(file.getName());
        Set<Project> createProjects = new HashSet<>();
        Set<String[]> failedValidationEntities = new HashSet<>();

        try(FileInputStream fileBytes = new FileInputStream(file);
             ) {
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

            int rowCount = 0;
            Row row = null;
            //to skip first row which contains additional information about cells
            if (rowCount == 0 && rows != null && rows.hasNext()) {
                rowCount++;
                row = rows.next(); // row number is 0
            }

            while (rows != null && rows.hasNext() && rowCount == 1) {
                row = rows.next();

                String[] projectData = new String[Constants.PROJECT_FILE_HEADERS.length];

                int cellCount = 0;
                while (cellCount < projectData.length) {
                    Cell cell = row.getCell(cellCount);

                    String cellValue = "";

                    cellValue = FileHelperFunctions.getCellValue(cell);

                    projectData[cellCount] = cellValue;

                    cellCount++;
                }

                if (!CustomPropValidators.areAllFieldsEmpty(projectData)) {
                    if (CustomPropValidators.isValidProjectFile(projectData)) {
                        createProjects.add(ProjectMapper.mapToProject(projectData));
                    } else {
                        failedValidationEntities.add(projectData);
                    }
                }
            }
        } catch (IOException e) {
            //will throw Internal Server Error

        } finally {
            //deliting temp file
            if (file.exists()) {
                file.delete();
            }
        }

        ResponseDto response = null;

        List<Long> dbResponse = projectRepository.saveAll(createProjects).stream().filter(Objects::nonNull).map(Project::getId).collect(Collectors.toList());

        if (createProjects.isEmpty() && !failedValidationEntities.isEmpty()) {
            response = new ResponseDto(
                failedValidationEntities,
                "Project/projects",
                " error. No entities to save into database",
                true);
        } else if(!failedValidationEntities.isEmpty()) {
            
            response = new ResponseDto(
                dbResponse, "Project/projects created successfully",
                failedValidationEntities, "These rows conatain errors. Please check"
            );
        }
        else {
            response = new ResponseDto(dbResponse, "Project/projects", false);
        }

        return response;
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findAll());
    }

    @Override
    public List<ProjectDto> getAllProjectsWithFutureTerminationDate() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findByFutureTerminationDate(DateTimeHelpers.getLocalDateNow()));
    }

    @Override
    public List<ProjectDto> getAllProjectsWithPriorTerminationDate() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findByPriorTerminationDate(DateTimeHelpers.getLocalDateNow()));
    }

    @Override
    public List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );

        List<Long> employeeProjectIds = getProjectIds(new ArrayList<Project>(employee.getProjects()));
        List<Project> projects = projectRepository.findByFutureTerminationDate(LocalDate.now());

        projects.removeIf(p -> employeeProjectIds.contains(p.getId()));

        return ProjectMapper.mapToListProjectsDto(projects);
    }

    @Override
    public List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );
        
        LocalDate currentDate = DateTimeHelpers.getLocalDateNow();

        if (date.getYear() < currentDate.getYear()||
            (date.getYear() == currentDate.getYear() && date.getMonthValue() < currentDate.getMonthValue())) {
            throw new CustomValidationException("Date", "date", date, "Date cannot be in past time");
        }

        List<Long> employeeProjectIds = getProjectIds(new ArrayList<Project>(employee.getProjects()));
        List<Project> projects = projectRepository.findByFutureTerminationDate(date);

        projects.removeIf(p -> employeeProjectIds.contains(p.getId()));

        return ProjectMapper.mapToListProjectsDto(projects);
    }

    @Override
    public List<ProjectDto> getAllProjectsByDevelopmentLanguage(Integer devLanguage) {
        CustomPropValidators.validateDevLang(devLanguage, resourceName);
        return ProjectMapper.mapToListProjectsDto(projectRepository.findByDevLanguage(devLanguage));
    }

    @Override
    public ProjectDto getProjectById(String projectId) {
        return ProjectMapper.mapToProjectDto(
            projectRepository.findById(Long.parseLong(projectId)).orElseThrow(() ->
                new ResourceNotFoundException(resourceName, "id", projectId)
            )
        );
    }

    @Override
    public void updateProject(Long projectId, EditProjectDto projectDto) {
        constraintViolationCheck(projectDto);

        Project existingProject = projectRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", projectId)
        );

        existingProject = ProjectMapper.mapToProject(existingProject, projectDto);

        projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project existingProject = projectRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", projectId)
        );

        removeProjectsFromEmployees(projectId, existingProject);
        
        projectRepository.delete(existingProject);
    }

    @Override
    public Resource generateExelFile() {
        String fileLocation = "";
        Path generatedFilePath = null;

        try(Workbook workBook = new XSSFWorkbook()) {
            
            LocalDate date = DateTimeHelpers.getLocalDateNow();
            Sheet workBookSheet = workBook.createSheet(Constants.PROJECT_FILE_NAME + date.toString());

            workBookSheet.setDefaultColumnWidth(100000);
            workBookSheet.setDefaultRowHeight((short) 500);
            
            Row header = workBookSheet.createRow(0);
            Cell headerCell = FileHelperFunctions.populateHeaderRow(header, Constants.PROJECT_FILE_HEADERS);

            List<Project> data = projectRepository.findAll();
                 
            if (data.isEmpty()) {
                throw new ResourceNotFoundException("Projects");
            }

            List<ProjectFileDto> projectsData = data
                .stream()
                .map(ProjectFileMapper::mapToProjectFileDto)
                .collect(Collectors.toList());

            Row row = null;
            Cell cell = null;

            for (int i = 1; i <= projectsData.size(); i++) {
                row = workBookSheet.createRow(i);
                ProjectFileDto project = projectsData.get(i-1);
                for (int j = 0; j < Constants.PROJECT_FILE_HEADERS.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(getProjectDataAtIndex(j, project));
                }
            }

            generatedFilePath = Paths.get(Constants.USER_DOCUMENTS_PATH + "\\" + workBookSheet.getSheetName() +  ".xlsx");
            fileLocation = generatedFilePath.toAbsolutePath().toString();

            try (FileOutputStream fos = new FileOutputStream(fileLocation)) {
                workBook.write(fos);
                workBook.close();
            }
        } catch (IOException e) {
            // will throw Internal Server Error
        }

        return FileHelperFunctions.generateUrlResource(generatedFilePath);
    }

    @PreRemove
    private void removeProjectsFromEmployees(Long projectId, Project project) {
        List<Employee> employees = employeeRepository.findByProjectId(projectId);

        employees.stream().forEach(e -> e.getProjects().remove(project));
        
        employeeRepository.saveAll(employees);
    }

    private List<Long> getProjectIds(List<Project> projects) {
        List<Long> projectIds = new ArrayList<>();

        projects.stream().forEach(p -> projectIds.add(p.getId()));

        return projectIds;
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
        String dataAtIndex = "";

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
            default:
                dataAtIndex = "";
        }

        return dataAtIndex;
    }
}
