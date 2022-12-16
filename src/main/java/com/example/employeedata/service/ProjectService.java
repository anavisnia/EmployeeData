package com.example.employeedata.service;

import java.util.*;

import com.example.employeedata.dto.*;

public interface ProjectService {
    ResponseDto saveProject(CreateProjectDto projectDto);
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long id);
    ProjectDto updateProject(Long id, EditProjectDto projectDto);
    void deleteProject(Long id);
}
