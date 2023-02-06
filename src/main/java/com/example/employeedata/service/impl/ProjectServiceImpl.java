package com.example.employeedata.service.impl;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;
import javax.validation.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.exception.*;
import com.example.employeedata.helpers.*;
import com.example.employeedata.mappers.ProjectMapper;
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

    @PreRemove
    private void removeProjectsFromEmployees(Long projectId, Project project) {
        List<Employee> employees = employeeRepository.findByProjectId(projectId);

        // for (Employee employee : employees) {
        //     employee.getProjects().remove(project);
        // }

        employees.stream().forEach(e -> e.getProjects().remove(project));
        
        employeeRepository.saveAll(employees);
    }

    private List<Long> getProjectIds(List<Project> projects) {
        List<Long> projectIds = new ArrayList<>();

        // for (Project project : projects) {
        //     projectIds.add(project.getId());
        // }

        projects.stream().forEach(p -> projectIds.add(p.getId()));

        return projectIds;
    }

    private <T> void constraintViolationCheck(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            // for (ConstraintViolation<T> constraintViolation : violations) {
            //     sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            // }

            violations.forEach(cv -> sb.append(cv.getPropertyPath() + " " + cv.getMessage() + ". "));

            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }
    }
    
}
