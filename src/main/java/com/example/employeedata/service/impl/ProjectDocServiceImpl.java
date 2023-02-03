package com.example.employeedata.service.impl;

import java.util.List;
import java.util.stream.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.document.ProjectDoc;
import com.example.employeedata.dto.*;
import com.example.employeedata.exception.*;
import com.example.employeedata.mappers.ProjectDocMapper;
import com.example.employeedata.repository.elastic.ProjectESRepository;
import com.example.employeedata.service.ProjectDocService;

@Service
public class ProjectDocServiceImpl implements ProjectDocService {

    private final ProjectESRepository projectESRepository;
    private final String resourceName = "ProjectDoc";

    public ProjectDocServiceImpl(ProjectESRepository projectESRepository) {
        this.projectESRepository = projectESRepository;
    }

    @Override
    public ResponseDto saveProject(CreateProjectDto projectDto) {
        ProjectDoc projectDoc = ProjectDocMapper.mapToProjectDoc(projectDto);

        ProjectDoc dbResponse = projectESRepository.save(projectDoc);

        return new ResponseDto(dbResponse.getId(), resourceName, false);
    }

    @Override
    public List<ProjectDoc> getAllProjects() {
        return StreamSupport.stream(projectESRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public ProjectDoc getProjectById(String projectId) {
        return projectESRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", projectId)
        );
    }

    @Override
    public void updateProject(String projectId, EditProjectDto editProjectDto) {
        ProjectDoc project = projectESRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", projectId)
        );

        projectESRepository.save(ProjectDocMapper.mapToProjectDoc(project, editProjectDto));
    }

    @Override
    public void deleteProject(String projectId) {
        ProjectDoc project = projectESRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", projectId)
        );

        projectESRepository.delete(project);
    }
    
}
