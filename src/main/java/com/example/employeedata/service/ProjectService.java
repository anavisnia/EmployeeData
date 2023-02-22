package com.example.employeedata.service;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;

@Service
public interface ProjectService {
    ResponseDto saveProject(CreateProjectDto projectDto);
    ResponseDto saveProjectsFromExelFile(MultipartFile multipartFile);
    List<ProjectDto> getAllProjects();
    PaginatedResponseDto<ProjectDto> getAllProjectsPage(String filter, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc);
    List<ProjectDto> getAllProjectsWithFutureTerminationDate();
    List<ProjectDto> getAllProjectsWithPriorTerminationDate();
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId);
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date);
    List<ProjectDto> getAllProjectsByDevelopmentLanguage(Integer devLanguage);
    ProjectDto getProjectById(String projectId);
    void updateProject(Long projectId, EditProjectDto projectDto);
    void deleteProject(Long projectId);
    byte[] generateExelFile();
    Map<String, List<ProjectDto>> getProjectsGroupedByDevLanguage();
}
