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
    private static final String RES_NAME = "ProjectDoc";
    private static final String ID = "id";

    public ProjectDocServiceImpl(ProjectESRepository projectESRepository) {
        this.projectESRepository = projectESRepository;
    }

    @Override
    public ResponseDto saveProject(CreateProjectDto projectDto) {
        ProjectDoc projectDoc = ProjectDocMapper.mapToProjectDoc(projectDto);

        ProjectDoc dbResponse = projectESRepository.save(projectDoc);

        return new ResponseDto(dbResponse.getId(), RES_NAME, false);
    }

    @Override
    public List<ProjectDoc> getAllProjects() {
        return StreamSupport.stream(projectESRepository.findAll()
            .spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public ProjectDoc getProjectById(String projectId) {
        return projectESRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, projectId)
        );
    }

    @Override
    public void updateProject(String projectId, EditProjectDto editProjectDto) {
        ProjectDoc project = projectESRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, projectId)
        );

        projectESRepository.save(ProjectDocMapper.mapToProjectDoc(project, editProjectDto));
    }

    @Override
    public void deleteProject(String projectId) {
        ProjectDoc project = projectESRepository.findById(projectId).orElseThrow(() ->
            new ResourceNotFoundException(RES_NAME, ID, projectId)
        );

        projectESRepository.delete(project);
    }
    
}
