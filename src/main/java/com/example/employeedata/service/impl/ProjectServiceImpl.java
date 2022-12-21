package com.example.employeedata.service.impl;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;
import javax.validation.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.exception.*;
import com.example.employeedata.helpers.DateTimeHelpers;
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
        Set<ConstraintViolation<CreateProjectDto>> violations = validator.validate(projectDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<CreateProjectDto> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }

        Project project = ProjectMapper.mapToProject(projectDto);
        Project dbResponse = projectRepository.save(project);

        return new ResponseDto(dbResponse.getId(), resourceName);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findAll());
    }

    @Override
    public List<ProjectDto> getAllProjectsWithFutureTerminationDate() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findProjectsWithFutureTerminationDate(DateTimeHelpers.getLocalDateNow()));
    }

    @Override
    public List<ProjectDto> getAllProjectsWithPriorTerminationDate() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findProjectsWithPriorTerminationDate(DateTimeHelpers.getLocalDateNow()));
    }

    @Override
    public List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );

        List<Long> employeeProjectIds = getProjectIds(new ArrayList<Project>(employee.getProjects()));
        List<Project> projects = projectRepository.findProjectsWithFutureTerminationDate(LocalDate.now());

        projects.removeIf(p -> employeeProjectIds.contains(p.getId()));

        return ProjectMapper.mapToListProjectsDto(projects);
    }

    @Override
    public List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );
        
        LocalDate currentDate = DateTimeHelpers.getLocalDateNow();

        if (date.getYear() < currentDate.getYear() && date.getMonthValue() < currentDate.getMonthValue()) {
            throw new CustomValidationException("Date", "date", date, "Date cannot be in past time");
        }

        List<Long> employeeProjectIds = getProjectIds(new ArrayList<Project>(employee.getProjects()));
        List<Project> projects = projectRepository.findProjectsWithFutureTerminationDate(date);

        projects.removeIf(p -> employeeProjectIds.contains(p.getId()));

        return ProjectMapper.mapToListProjectsDto(projects);
    }

    @Override
    public ProjectDto getProjectById(Long projectId) {
        return ProjectMapper.mapToProjectDto(
            projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException(resourceName, "id", projectId)
            )
        );
    }

    @Override
    public ProjectDto updateProject(Long projectId, EditProjectDto projectDto) {
        Set<ConstraintViolation<EditProjectDto>> violations = validator.validate(projectDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<EditProjectDto> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }

        Project existingProject = projectRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", projectId)
        );

        existingProject = ProjectMapper.mapToProject(existingProject, projectDto);

        return ProjectMapper.mapToProjectDto(projectRepository.save(existingProject));
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
        List<Employee> employees = employeeRepository.findAllEmployeesByProjectId(projectId);
        for (Employee employee : employees) {
            employee.getProjects().remove(project);
        }
        employeeRepository.saveAll(employees);
    }

    private List<Long> getProjectIds(List<Project> projects) {
        List<Long> projectIds = new ArrayList<>();

        for (Project project : projects) {
            projectIds.add(project.getId());
        }

        return projectIds;
    }
    
}
