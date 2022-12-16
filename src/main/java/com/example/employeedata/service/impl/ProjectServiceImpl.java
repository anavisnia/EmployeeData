package com.example.employeedata.service.impl;

import java.util.*;

import javax.persistence.*;
import javax.validation.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.exception.*;
import com.example.employeedata.mappers.ProjectMapper;
import com.example.employeedata.repository.*;
import com.example.employeedata.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Validator validator;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final String resourceErrName = "Project";

    public ProjectServiceImpl(Validator validator,
            ProjectRepository projectRepository,
            EmployeeRepository employeeRepository) {
        this.validator = validator;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ProjectDto saveProject(CreateProjectDto projectDto) {
        Set<ConstraintViolation<CreateProjectDto>> violations = validator.validate(projectDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<CreateProjectDto> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }

        Project project = ProjectMapper.mapToProject(projectDto);
        return ProjectMapper.mapToProjectDto(projectRepository.save(project));
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMapper.mapToListProjectsDto(projectRepository.findAll());
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        return ProjectMapper.mapToProjectDto(
            projectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(resourceErrName, "id", id)
            )
        );
    }

    @Override
    public ProjectDto updateProject(Long id, EditProjectDto projectDto) {
        Set<ConstraintViolation<EditProjectDto>> violations = validator.validate(projectDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<EditProjectDto> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }

        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceErrName, "id", id)
        );

        existingProject = ProjectMapper.mapToProject(existingProject, projectDto);

        return ProjectMapper.mapToProjectDto(projectRepository.save(existingProject));
    }

    @Override
    public void deleteProject(Long id) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceErrName, "id", id)
        );

        removeProjectsFromEmployees(id, existingProject);
        
        projectRepository.delete(existingProject);
    }

    @PreRemove
    private void removeProjectsFromEmployees(Long id, Project project) {
        List<Employee> employees = employeeRepository.findAllEmployeesByProjectId(id);
        for (Employee employee : employees) {
            employee.getProjects().remove(project);
        }
        employeeRepository.saveAll(employees);
    }
    
}
