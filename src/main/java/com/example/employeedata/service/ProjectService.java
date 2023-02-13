package com.example.employeedata.service;

import java.time.*;
import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;

@Service
public interface ProjectService {
    ResponseDto saveProject(CreateProjectDto projectDto);
    ResponseDto saveProjectsFromExelFile(MultipartFile multipartFile);
    List<ProjectDto> getAllProjects();
    PaginatedResponseDto<ProjectDto> getAllProjectsPageable(Integer pageNumber, Integer pageSize, String filter, String isAsc);
    PaginatedResponseDto<ProjectDto> getAllProjectsPageableAndFiltered(String query, Integer pageNumber, Integer pageSize, String filter, String isAsc);
    List<ProjectDto> getAllProjectsWithFutureTerminationDate();
    List<ProjectDto> getAllProjectsWithPriorTerminationDate();
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId);
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date);
    List<ProjectDto> getAllProjectsByDevelopmentLanguage(Integer devLanguage);
    ProjectDto getProjectById(String projectId);
    void updateProject(Long projectId, EditProjectDto projectDto);
    void deleteProject(Long projectId);
    Resource generateExelFile();
}
