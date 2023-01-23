package com.example.employeedata.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employeedata.document.ProjectDoc;
import com.example.employeedata.dto.*;

@Service
public interface ProjectDocService {
    ResponseDto saveProject(CreateProjectDto projectDto);
    List<ProjectDoc> getAllProjects();
    ProjectDoc getProjectById(String projectId);
    void updateProject(String projectId, EditProjectDto editProjectDto);
    void deleteProject(String projectId);
}
