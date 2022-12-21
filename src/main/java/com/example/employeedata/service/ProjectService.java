package com.example.employeedata.service;

import java.time.*;
import java.util.*;

import com.example.employeedata.dto.*;

public interface ProjectService {
    ResponseDto saveProject(CreateProjectDto projectDto);
    List<ProjectDto> getAllProjects();
    List<ProjectDto> getAllProjectsWithFutureTerminationDate();
    List<ProjectDto> getAllProjectsWithPriorTerminationDate();
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId);
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date);
    ProjectDto getProjectById(Long projectId);
    ProjectDto updateProject(Long projectId, EditProjectDto projectDto);
    void deleteProject(Long projectId);
}
