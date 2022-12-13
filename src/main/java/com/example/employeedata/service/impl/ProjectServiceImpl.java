package com.example.employeedata.service.impl;

import java.util.*;

import javax.validation.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.dto.ProjectDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.exception.*;
import com.example.employeedata.repository.*;
import com.example.employeedata.service.ProjectService;
import com.example.employeedata.service.helpers.ProjectMapper;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Validator validator;
    private final ProjectRepository projectRepository;
    private final String resourceErrName = "Project";
    
    public ProjectServiceImpl(Validator validator, ProjectRepository projectRepository) {
        this.validator = validator;
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(ProjectDto projectDto) {
        Set<ConstraintViolation<ProjectDto>> violations = validator.validate(projectDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<ProjectDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        Project project = ProjectMapper.mapToProject(projectDto);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project getById(long id) {
        return projectRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceErrName, "id", id)
        );
    }

    @Override
    public Project update(long id, ProjectDto projectDto) {
        Set<ConstraintViolation<ProjectDto>> violations = validator.validate(projectDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<ProjectDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceErrName, "id", id)
        );

        existingProject = ProjectMapper.mapToProject(existingProject, projectDto);

        return projectRepository.save(existingProject);
    }

    @Override
    public void delete(long id) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceErrName, "id", id)
        );

        projectRepository.delete(existingProject);
    }
    
}
